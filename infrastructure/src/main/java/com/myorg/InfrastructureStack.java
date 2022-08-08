package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.secretsmanager.Secret;
import software.amazon.awscdk.CfnOutput;

public class InfrastructureStack extends Stack {
    public final Secret app_sm;

    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        final Table qr_code_table = Table.Builder.create(this, "QRCodeTable")
                .tableName("QRCodeTable")
                .partitionKey(getPartitionKey("email"))
                .build();
        
        final Table invocation_table = Table.Builder.create(this, "InvocationTable")
                .tableName("InvocationTable")
                .partitionKey(getPartitionKey("qrcode_id"))
                .build();
        
        final Table user_table = Table.Builder.create(this, "UserTable")
                .tableName("UserTable")
                .partitionKey(getPartitionKey("email"))
                .build();
            
        final Table dynamic_salt_table = Table.Builder.create(this, "DynamicSaltTable")
                .tableName("DynamicSaltTable")
                .partitionKey(getPartitionKey("email"))
                .build();

        final Secret qrcode_secrets = Secret.Builder.create(this, "QRCodeSM")
                .build();
        this.app_sm = qrcode_secrets;
        
        CfnOutput.Builder.create(this, "QRCodeSMOP")
                .value(qrcode_secrets.getSecretArn())
                .build();
    }

    private Attribute getPartitionKey(String keyname) {
        return Attribute.builder().name(keyname).type(AttributeType.STRING).build();
    }
}
