/*
 * Copyright (c) 2016 ingenieux Labs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.ingenieux.mojo.beanstalk.env;

import com.amazonaws.services.elasticbeanstalk.model.ConfigurationOptionSetting;
import com.amazonaws.services.elasticbeanstalk.model.OptionSpecification;

import org.apache.maven.plugin.AbstractMojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import br.com.ingenieux.mojo.beanstalk.AbstractNeedsEnvironmentMojo;
import br.com.ingenieux.mojo.beanstalk.cmd.env.update.UpdateEnvironmentCommand;
import br.com.ingenieux.mojo.beanstalk.cmd.env.update.UpdateEnvironmentContext;
import br.com.ingenieux.mojo.beanstalk.cmd.env.update.UpdateEnvironmentContextBuilder;

/**
 * Updates the environment versionLabel for a given environmentName <p/> See the <a href=
 * "http://docs.amazonwebservices.com/elasticbeanstalk/latest/api/API_UpdateEnvironment.html"
 * >UpdateEnvironment API</a> call.
 *
 * @since 0.2.0
 */
@Mojo(name = "update-environment")
public class UpdateEnvironmentMojo extends AbstractNeedsEnvironmentMojo {

  /**
   * Version Label to use
   */
  @Parameter(property = "beanstalk.versionLabel")
  String versionLabel;

  /**
   * Application Description
   */
  @Parameter(property = "beanstalk.environmentDescription")
  String environmentDescription;

  /**
   * <p> Configuration Option Settings. Will evaluate as such: </p> <p/> <p> If empty, will lookup
   * for beanstalk.env.aws.x.y variable in the context, and it will map this variable to namespace
   * [aws.x], under option name y, unless there's an alias set. </p> <p/> <p> A Property might be
   * aliased. Current aliases include: </p> <p/> <ul> <li>beanstalk.scalingAvailabilityZones, to
   * aws:autoscaling:asg/Availability Zones</li> <li>beanstalk.scalingCooldown, to
   * aws:autoscaling:asg/Cooldown</li> <li>beanstalk.scalingCustomAvailabilityZones, to
   * aws:autoscaling:asg/Custom Availability Zones</li> <li>beanstalk.scalingMinSize, to
   * aws:autoscaling:asg/MinSize</li> <li>beanstalk.scalingMaxSize, to
   * aws:autoscaling:asg/MaxSize</li> <li>beanstalk.availabilityZones, to
   * aws:autoscaling:asg/Custom Availability Zones (SAME AS beanstalk.scalingCustomAvailabilityZones
   * FOR BACKWARD COMPATIBILITY</li> <p/> <li>beanstalk.keyName, to aws:autoscaling:launchconfiguration/EC2KeyName
   * (EC2 Instance Key)</li> <li>beanstalk.iamInstanceProfile, to aws:autoscaling:launchconfiguration/IamInstanceProfile
   * (IAM Instance Profile Role Name)</li> <li>beanstalk.imageId, to
   * aws:autoscaling:launchconfiguration/ImageId</li> <li>beanstalk.instanceType, to
   * aws:autoscaling:launchconfiguration/InstanceType (EC2 Instance Type to Use)</li>
   * <li>beanstalk.monitoringInterval, to aws:autoscaling:launchconfiguration/MonitoringInterval</li>
   * <li>beanstalk.securityGroups, to aws:autoscaling:launchconfiguration/SecurityGroups</li>
   * <li>beanstalk.sshSourceRestriction, to aws:autoscaling:launchconfiguration/SSHSourceRestriction</li>
   * <li>beanstalk.blockDeviceMappings, to aws:autoscaling:launchconfiguration/BlockDeviceMappings</li>
   * <li>beanstalk.rootVolumeType, to aws:autoscaling:launchconfiguration/RootVolumeType</li>
   * <li>beanstalk.rootVolumeSize, to aws:autoscaling:launchconfiguration/RootVolumeSize</li>
   * <li>beanstalk.rootVolumeIOPS, to aws:autoscaling:launchconfiguration/RootVolumeIOPS</li> <p/>
   * <li>beanstalk.triggerBreachDuration, to aws:autoscaling:trigger/BreachDuration</li>
   * <li>beanstalk.triggerLowerBreachScaleIncrement, to aws:autoscaling:trigger/LowerBreachScaleIncrement</li>
   * <li>beanstalk.triggerLowerThreshold, to aws:autoscaling:trigger/LowerThreshold</li>
   * <li>beanstalk.triggerMeasureName, to aws:autoscaling:trigger/MeasureName</li>
   * <li>beanstalk.triggerPeriod, to aws:autoscaling:trigger/Period</li>
   * <li>beanstalk.triggerStatistic, to aws:autoscaling:trigger/Statistic</li>
   * <li>beanstalk.triggerUnit, to aws:autoscaling:trigger/Unit</li>
   * <li>beanstalk.triggerUpperBreachScaleIncrement, to aws:autoscaling:trigger/UpperBreachScaleIncrement</li>
   * <li>beanstalk.triggerUpperThreshold, to aws:autoscaling:trigger/UpperThreshold</li> <p/>
   * <li>beanstalk.rollingupdateMaxBatchSize, to aws:autoscaling:updatepolicy:rollingupdate/MaxBatchSize</li>
   * <li>beanstalk.rollingupdateMinInstancesInService, to aws:autoscaling:updatepolicy:rollingupdate/MinInstancesInService</li>
   * <li>beanstalk.rollingupdatePauseTime, to aws:autoscaling:updatepolicy:rollingupdate/PauseTime</li>
   * <li>beanstalk.rollingupdateEnabled, to aws:autoscaling:updatepolicy:rollingupdate/RollingUpdateEnabled</li>
   * <p/> <li>beanstalk.vpcId, to aws:ec2:vpc/VPCId</li> <li>beanstalk.vpcSubnets, to
   * aws:ec2:vpc/Subnets</li> <li>beanstalk.vpcELBSubnets, to aws:ec2:vpc/ELBSubnets</li>
   * <li>beanstalk.vpcELBScheme, to aws:ec2:vpc/ELBScheme</li> <li>beanstalk.vpcDBSubnets, to
   * aws:ec2:vpc/DBSubnets</li> <li>beanstalk.vpcAssociatePublicIpAddress, to
   * aws:ec2:vpc/AssociatePublicIpAddress</li> <p/> <li>beanstalk.applicationHealthCheckURL, to
   * aws:elasticbeanstalk:application/Application Healthcheck URL (Application Healthcheck
   * URL)</li> <p/> <li>beanstalk.timeout, to aws:elasticbeanstalk:command/Timeout</li> <p/>
   * <li>beanstalk.environmentType, to aws:elasticbeanstalk:environment/EnvironmentType
   * (SingleInstance or ELB-bound Environment)</li> <p/> <li>beanstalk.automaticallyTerminateUnhealthyInstances,
   * to aws:elasticbeanstalk:monitoring/Automatically Terminate Unhealthy Instances (true if
   * should automatically terminate instances)</li> <p/> <li>beanstalk.notificationEndpoint, to
   * aws:elasticbeanstalk:sns:topics/Notification Endpoint</li> <li>beanstalk.notificationProtocol,
   * to aws:elasticbeanstalk:sns:topics/Notification Protocol</li> <li>beanstalk.notificationTopicARN,
   * to aws:elasticbeanstalk:sns:topics/Notification Topic ARN</li> <li>beanstalk.notificationTopicName,
   * to aws:elasticbeanstalk:sns:topics/Notification Topic Name</li> <p/>
   * <li>beanstalk.sqsdWorkerQueueUrl, to aws:elasticbeanstalk:sqsd/WorkerQueueURL</li>
   * <li>beanstalk.sqsdHttpPath, to aws:elasticbeanstalk:sqsd/HttpPath</li>
   * <li>beanstalk.sqsdMimeType, to aws:elasticbeanstalk:sqsd/MimeType</li>
   * <li>beanstalk.sqsdHttpConnections, to aws:elasticbeanstalk:sqsd/HttpConnections</li>
   * <li>beanstalk.sqsdConnectTimeout, to aws:elasticbeanstalk:sqsd/ConnectTimeout</li>
   * <li>beanstalk.sqsdInactivityTimeout, to aws:elasticbeanstalk:sqsd/InactivityTimeout</li>
   * <li>beanstalk.sqsdVisibilityTimeout, to aws:elasticbeanstalk:sqsd/VisibilityTimeout</li>
   * <li>beanstalk.sqsdRetentionPeriod, to aws:elasticbeanstalk:sqsd/RetentionPeriod</li>
   * <li>beanstalk.sqsdMaxRetries, to aws:elasticbeanstalk:sqsd/MaxRetries</li> <p/>
   * <li>beanstalk.healthcheckHealthyThreshold, to aws:elb:healthcheck/HealthyThreshold</li>
   * <li>beanstalk.healthcheckInterval, to aws:elb:healthcheck/Interval</li>
   * <li>beanstalk.healthcheckTimeout, to aws:elb:healthcheck/Timeout</li>
   * <li>beanstalk.healthcheckUnhealthyThreshold, to aws:elb:healthcheck/UnhealthyThreshold</li>
   * <p/> <li>beanstalk.loadBalancerHTTPPort, to aws:elb:loadbalancer/LoadBalancerHTTPPort</li>
   * <li>beanstalk.loadBalancerPortProtocol, to aws:elb:loadbalancer/LoadBalancerPortProtocol</li>
   * <li>beanstalk.loadBalancerHTTPSPort, to aws:elb:loadbalancer/LoadBalancerHTTPSPort</li>
   * <li>beanstalk.loadBalancerSSLPortProtocol, to aws:elb:loadbalancer/LoadBalancerSSLPortProtocol</li>
   * <li>beanstalk.loadBalancerSSLCertificateId, to aws:elb:loadbalancer/SSLCertificateId</li>
   * <p/> <li>beanstalk.stickinessCookieExpiration, to aws:elb:policies/Stickiness Cookie
   * Expiration (Stickiness Cookie Expiration Timeout)</li> <li>beanstalk.stickinessPolicy, to
   * aws:elb:policies/Stickiness Policy (ELB Stickiness Policy)</li> <p/>
   * <li>beanstalk.dbAllocatedStorage, to aws:rds:dbinstance/DBAllocatedStorage</li>
   * <li>beanstalk.dbDeletionPolicy, to aws:rds:dbinstance/DBDeletionPolicy</li>
   * <li>beanstalk.dbEngine, to aws:rds:dbinstance/DBEngine</li> <li>beanstalk.dbEngineVersion, to
   * aws:rds:dbinstance/DBEngineVersion</li> <li>beanstalk.dbInstanceClass, to
   * aws:rds:dbinstance/DBInstanceClass</li> <li>beanstalk.dbPassword, to
   * aws:rds:dbinstance/DBPassword</li> <li>beanstalk.dbSnapshotIdentifier, to
   * aws:rds:dbinstance/DBSnapshotIdentifier</li> <li>beanstalk.dbUser, to
   * aws:rds:dbinstance/DBUser</li> <li>beanstalk.dbMultiAZDatabase, to
   * aws:rds:dbinstance/MultiAZDatabase</li> <p/> <li>beanstalk.environmentAwsSecretKey, to
   * aws:elasticbeanstalk:application:environment/AWS_SECRET_KEY</li>
   * <li>beanstalk.environmentAwsAccessKeyId, to aws:elasticbeanstalk:application:environment/AWS_ACCESS_KEY_ID</li>
   * <li>beanstalk.environmentJdbcConnectionString, to aws:elasticbeanstalk:application:environment/JDBC_CONNECTION_STRING</li>
   * <li>beanstalk.environmentParam1, to aws:elasticbeanstalk:application:environment/PARAM1</li>
   * <li>beanstalk.environmentParam2, to aws:elasticbeanstalk:application:environment/PARAM2</li>
   * <li>beanstalk.environmentParam3, to aws:elasticbeanstalk:application:environment/PARAM3</li>
   * <li>beanstalk.environmentParam4, to aws:elasticbeanstalk:application:environment/PARAM4</li>
   * <li>beanstalk.environmentParam5, to aws:elasticbeanstalk:application:environment/PARAM5</li>
   * <p/> <li>beanstalk.logPublicationControl, to aws:elasticbeanstalk:hostmanager/LogPublicationControl</li>
   * <p/> <li>beanstalk.jvmOptions, to aws:elasticbeanstalk:container:tomcat:jvmoptions/JVM
   * Options</li> <li>beanstalk.jvmXmx, to aws:elasticbeanstalk:container:tomcat:jvmoptions/Xmx</li>
   * <li>beanstalk.jvmMaxPermSize, to aws:elasticbeanstalk:container:tomcat:jvmoptions/XX:MaxPermSize</li>
   * <li>beanstalk.jvmXms, to aws:elasticbeanstalk:container:tomcat:jvmoptions/Xms</li> <p/>
   * <li>beanstalk.phpDocumentRoot, to aws:elasticbeanstalk:container:php:phpini/document_root</li>
   * <li>beanstalk.phpMemoryLimit, to aws:elasticbeanstalk:container:php:phpini/memory_limit</li>
   * <li>beanstalk.phpZlibOutputCompression, to aws:elasticbeanstalk:container:php:phpini/zlib.output_compression</li>
   * <li>beanstalk.phpAllowUrlFopen, to aws:elasticbeanstalk:container:php:phpini/allow_url_fopen</li>
   * <li>beanstalk.phpDisplayErrors, to aws:elasticbeanstalk:container:php:phpini/display_errors</li>
   * <li>beanstalk.phpMaxExecutionTime, to aws:elasticbeanstalk:container:php:phpini/max_execution_time</li>
   * <li>beanstalk.phpComposerOptions, to aws:elasticbeanstalk:container:php:phpini/composer_options</li>
   * </ul> <p/> The reason for most of those aliases if the need to address space and ':' inside
   * Maven Properties and XML Files.
   */
  @Parameter ConfigurationOptionSetting[] optionSettings;

  @Parameter OptionSpecification[] optionsToRemove;

  /**
   * <p> Template Name. </p> <p/> <p> Could be either literal or a glob, like, <p/>
   * <pre>
   * ingenieux-services-prod-*
   * </pre>
   * <p/> . If a glob, there will be a lookup involved, and the first one in reverse ASCIIbetical
   * order will be picked upon. </p>
   */
  @Parameter(property = "beanstalk.templateName")
  String templateName;

  /**
   * <p>Environment Tier Name (defaults to "WebServer")</p>
   */
  @Parameter(property = "beanstalk.environmentTierName", defaultValue = "WebServer")
  String environmentTierName;

  /**
   * <p>Environment Tier Type</p>
   */
  @Parameter(property = "beanstalk.environmentTierType")
  String environmentTierType;

  /**
   * <p>Environment Tier Version</p>
   */
  @Parameter(property = "beanstalk.environmentTierVersion")
  String environmentTierVersion;

  protected Object executeInternal() throws AbstractMojoExecutionException {
    versionLabel = lookupVersionLabel(applicationName, versionLabel);

    waitForNotUpdating();

    if (null == optionSettings) {
      optionSettings = super.introspectOptionSettings();
    }

    UpdateEnvironmentContextBuilder builder =
        UpdateEnvironmentContextBuilder.updateEnvironmentContext()
            .withEnvironmentId(curEnv.getEnvironmentId()) //
            .withEnvironmentDescription(environmentDescription) //
            .withEnvironmentName(curEnv.getEnvironmentName()) //
            .withOptionSettings(optionSettings) //
            .withOptionsToRemove(optionsToRemove) //
            .withTemplateName(lookupTemplateName(applicationName, templateName)) //
            .withVersionLabel(versionLabel) //
            .withEnvironmentTierType(environmentTierType) //
            .withEnvironmentTierName(environmentTierName) //
            .withEnvironmentTierVersion(environmentTierVersion) //
            .withLatestVersionLabel(curEnv.getVersionLabel());

    UpdateEnvironmentContext context = builder.build();
    UpdateEnvironmentCommand command = new UpdateEnvironmentCommand(this);

    return command.execute(context);
  }
}
