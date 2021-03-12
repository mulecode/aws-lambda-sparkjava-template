provider "aws" {
  region = var.region
}

terraform {
  backend "s3" {
    encrypt = true
  }
}

locals {
  context_path = "/spark"
  handler = "uk.co.mulecode.lambda.ApplicationRequestHandler"
  description = "my lambda"
  artifact_path = "../build/distributions/${var.function_name}.zip"
  policy = <<JSON
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "ec2:DescribeNetworkInterfaces",
        "ec2:CreateNetworkInterface",
        "ec2:DeleteNetworkInterface",
        "ec2:DescribeInstances",
        "ec2:AttachNetworkInterface"
      ],
      "Resource": "*"
    },
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "*"
    }
  ]
}
JSON
}


data "terraform_remote_state" "context_vpc" {
  backend = "s3"
  config = {
    bucket = var.state_bucket
    key = "vpc/main.tfstate"
    region = var.region
  }
}

data "terraform_remote_state" "api" {
  backend = "s3"
  config = {
    bucket = var.state_bucket
    key = "api/main.tfstate"
    region = var.region
  }
}

module "lambda_function" {
  source = "github.com/mulecode/terraform.git//module/lambda"
  name = var.function_name
  description = local.description
  handler = local.handler
  runtime = "java11"
  file_path = local.artifact_path
  vpc_id = data.terraform_remote_state.context_vpc.outputs.vpc_id
  vpc_subnets = data.terraform_remote_state.context_vpc.outputs.public_subnets
  iam_policy = local.policy
  timeout_seconds = 30
  environment_variables = {
    "profile" : "dev"
  }
}

module "lambda_integration" {
  source = "github.com/mulecode/terraform.git//module/api_gateway_lambda_integration"
  depends_on = [
    module.lambda_function]
  apigateway_id = data.terraform_remote_state.api.outputs.apigateway_api_id
  apigateway_execution_arn = data.terraform_remote_state.api.outputs.apigateway_api_execution_arn
  lambda_arn = module.lambda_function.lambda_arn
  lambda_invoke_arn = module.lambda_function.lambda_invoke_arn
}

module "route_default" {
  source = "github.com/mulecode/terraform.git//module/api_gateway_route"
  depends_on = [
    module.lambda_integration]
  apigateway_api_id = data.terraform_remote_state.api.outputs.apigateway_api_id
  apigateway_integration_id = module.lambda_integration.apigateway_integration_id
  route_key = "ANY ${local.context_path}/{proxy+}"
}
