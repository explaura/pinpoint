# Pinpoint

## Requirements
- saml2aws
- amplify CLI

## Getting started with Pinpoint

1. Login to AWS using `saml2aws`:
```bash
saml2aws login
```

2. Initialise project with Amplify
```bash
amplify init
```

2. Register Pinpoint 
```bash
amplify add analytics
```

3. Create analytics in AWS
```bash
amplify push
```

**Note that Pinpoint contains a region mapping. For `ap-southeast-2` region the Pinpoint analytics are created in `us-west-2`**

## Endpoints

Add an imported endpoint:
```bash
aws pinpoint update-endpoint \
    --application-id 5b779659e645452d8acc6fe293918eb8 \
    --endpoint-id laura-mobile-endpoint \
    --endpoint-request file://endpoint-request-file.json
```

## Learnings

- Endpoints: 
    - a destination/recipient of message
        - can be a mobile device/phone number/email
    - a user can have one or more endpoints
    - before messaging a user, you must have at least one endpoint for that user
    - requires a specific schema
        - https://docs.aws.amazon.com/pinpoint/latest/apireference/apps-application-id-endpoints-endpoint-id.html#apps-application-id-endpoints-endpoint-id-schemas
    - two types:
        - dynamic - added/updated by apps
        - imported - a dump of endpoints from an external source e.g. migrated from another platform
- Filters:
    - ability to specify attributes in order to start narrowing down the types of endpoints you want to target
- Segments:
    - a group of endpoints which has 0 or more filters applied
    - get a live view (dynamic endpoints only) of how many users you are targeting based off filters applied to segment
- Events:
    - integrated apps provide usage data (events)
    - view events in the Pinpoint console to view how/when/how often users are using the app