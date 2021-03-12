# aws-lambda-sparkjava-template

Sample template of sparkjava lambda and API Gateway V2

Includes:
- Secret Manager Service

### Setup

**Replace default `aws-lambda-sparkjava-template` to your project name.**

settings.gradle.kts
```kotlin
rootProject.name = "aws-lambda-sparkjava-template"
```

./tf/backend_config/dev.tfvars
```hcl-terraform
key = "aws-lambda-sparkjava-template/main.tfstate"
```

**Replace java package name**

./src/main/java/<your_package>
```
uk.co.mulecode.lambda
```

build.gradle.kts
```
group = "uk.co.mulecode.lambda"
```

**Replace terraform S3 state bucket account Ids**

./tf/backend_config/dev.tfvars
```hcl-terraform
bucket = "terraform-<ACCOUNT_ID>"
```

./tf/env_vars/dev.tfvars
```hcl-terraform
state_bucket = "terraform-<ACCOUNT_ID>"
```

**Lambda handler class**

./td/main.tf 
```hcl-terraform
locals {
  handler = "uk.co.mulecode.lambda.ApplicationRequestHandler"
  description = "my lambda"
}
```

**Application context name**

./td/main.tf 
```hcl-terraform
locals {
  context_path = "/spark"
}
```

./src/main/java/<your_package>/resource/SparkResources.java
```
public static final String CONTEXT_PATH = "/spark";
```

Important! context path name must match in terraform apiGatewayV2 and spark resources.
