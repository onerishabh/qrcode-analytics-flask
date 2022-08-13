package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;
import java.io.IOException;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

// example test. To run these tests, uncomment this file, along with the
// example resource in java/src/main/java/com/myorg/InfrastructureStack.java
public class InfrastructureTest {

    @Test
    public void testDynamoDbTables() throws IOException {
        App app = new App();
        InfrastructureStack stack = new InfrastructureStack(app, "test");

        Template template = Template.fromStack(stack);

        template.hasResourceProperties("AWS::DynamoDB::Table", new HashMap<String, String>() {{
            put("TableName", "QRCodeTable");
          }});

        template.hasResourceProperties("AWS::DynamoDB::Table", new HashMap<String, String>() {{
            put("TableName", "InvocationTable");
          }});

        template.hasResourceProperties("AWS::DynamoDB::Table", new HashMap<String, String>() {{
        put("TableName", "UserTable");
        }});

        template.hasResourceProperties("AWS::DynamoDB::Table", new HashMap<String, String>() {{
        put("TableName", "DynamicSaltTable");
        }});

    }

    @Test
    public void testSecretsManager() throws IOException {
        App app = new App();
        InfrastructureStack stack = new InfrastructureStack(app, "test");

        Template template = Template.fromStack(stack);

        template.resourceCountIs("AWS::SecretsManager::Secret", 1);
    }
}
