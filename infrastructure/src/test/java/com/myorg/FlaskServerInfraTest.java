package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.assertions.Template;
import java.io.IOException;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import java.util.Collections;
import java.util.Map;

public class FlaskServerInfraTest {
    
    @Test
    public void testSecurityGroup() throws IOException {
        App app = new App();
        QRCodeFlaskServerStack stack = new QRCodeFlaskServerStack(app, "test");
        Template template = Template.fromStack(stack);

        template.hasResource("AWS::EC2::SecurityGroup", Map.of(
            "IpPermissions", Collections.singletonList(Map.of(
                "FromPort", 5000,
                "IpProtocol", "tcp"
            ))
        ));
    }
}
