package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

import java.util.Arrays;

public class InfrastructureApp {
    public static void main(final String[] args) {
        App app = new App();

        new InfrastructureStack(app, "InfrastructureStack", StackProps.builder()
                .build());
        
        new QRCodeFlaskServerStack(app, "QRCodeFlaskServerStack", StackProps.builder()
                .build());

        app.synth();
    }
}

