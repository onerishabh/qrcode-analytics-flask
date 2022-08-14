package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class FlaskServerInfraTest {
    
    @Test
    public void testSecurityGroup() throws IOException {
        App app = new App();
        QRCodeFlaskServerStack stack = new QRCodeFlaskServerStack(app, "test");
        Template template = Template.fromStack(stack);

        template.resourceCountIs("AWS::EC2::SecurityGroup", 1);

    }
}
