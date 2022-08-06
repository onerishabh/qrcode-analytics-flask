package com.myorg;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.FargateService;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Peer;
import software.amazon.awscdk.services.ec2.Port;
import java.util.UUID;

public class QRCodeFlaskServerStack extends Stack {
    public QRCodeFlaskServerStack(final Construct scope, final String id) {
        this(scope, id, null, null);
    }

    public QRCodeFlaskServerStack(final Construct scope, final String id, final StackProps props, String secret_arn) {
        super(scope, id, props);

        final Cluster ecs_cluster = Cluster.Builder.create(this, "ClusterService")
                    .build();
        
        final FargateTaskDefinition task_def = FargateTaskDefinition.Builder.create(this, "QRCodeServerTask")
                    .build();
        PortMapping pm = PortMapping.builder()
                    .containerPort(5000)
                    .hostPort(5000)
                    .build();

        Map<String, String> hm = new HashMap<String, String>();
        hm.put("AWS_DEFAULT_REGION", "us-east-1");
        hm.put("AWS_ACCESS_KEY_ID",  System.getenv("AWS_ACCESS_KEY_ID"));
        hm.put("AWS_SECRET_ACCESS_KEY",  System.getenv("AWS_SECRET_ACCESS_KEY"));
        hm.put("SECRETS_MANAGER", secret_arn);
        hm.put("STATIC_SALT", get_static_salt());
            
        task_def.addContainer("QRCode", ContainerDefinitionOptions.builder()
                    .image(ContainerImage.fromAsset("../server"))
                    .environment(hm)
                    .portMappings(Arrays.asList(pm))
                    .build());

        final SecurityGroup sg = SecurityGroup.Builder.create(this, "ECSTaskSG")
                    .allowAllOutbound(true)
                    .vpc(ecs_cluster.getVpc())
                    .build();
        sg.addIngressRule(Peer.anyIpv4(), Port.tcp(5000));
        final FargateService ecs_fargate = FargateService.Builder.create(this, "QRCodeService")
                    .cluster(ecs_cluster)
                    .securityGroups(Arrays.asList(sg))
                    .taskDefinition(task_def).assignPublicIp(true)
                    .desiredCount(1)
                    .build();
        
    }
    private String get_static_salt(){
        String salt = UUID.randomUUID().toString();
        return salt;
    }
}