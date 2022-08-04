package com.myorg;
import java.util.Arrays;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ecs.Cluster;
import software.amazon.awscdk.services.ecs.FargateTaskDefinition;
import software.amazon.awscdk.services.ecs.ContainerDefinitionOptions;
import software.amazon.awscdk.services.ecs.ContainerImage;
import software.amazon.awscdk.services.ecs.FargateService;
import software.amazon.awscdk.services.ecs.PortMapping;
import software.amazon.awscdk.CfnOutput;

public class QRCodeFlaskServerStack extends Stack {
    public QRCodeFlaskServerStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public QRCodeFlaskServerStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Cluster ecs_cluster = Cluster.Builder.create(this, "ClusterService")
                    .build();
        
        final FargateTaskDefinition task_def = FargateTaskDefinition.Builder.create(this, "QRCodeServerTask")
                    .build();
        PortMapping pm = PortMapping.builder()
                    .containerPort(5000)
                    .hostPort(5000)
                    .build();
            
        task_def.addContainer("QRCode", ContainerDefinitionOptions.builder()
                    .image(ContainerImage.fromAsset("../server"))
                    .portMappings(Arrays.asList(pm))
                    .build());

        final FargateService ecs_fargate = FargateService.Builder.create(this, "QRCodeService")
                    .cluster(ecs_cluster)
                    .taskDefinition(task_def).assignPublicIp(true)
                    .desiredCount(1)
                    .build();
        
    }
}