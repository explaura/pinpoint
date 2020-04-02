# Pinpoint

## Requirements
- saml2aws

## Pinpoint Endpoints

1. Login to AWS using `saml2aws`:
```bash
saml2aws login
```

2. Add an endpoint:
```bash
aws pinpoint update-endpoint \
    --application-id 5b779659e645452d8acc6fe293918eb8 \
    --endpoint-id laura-mobile-endpoint \
    --endpoint-request file://endpoint-request-file.json
```