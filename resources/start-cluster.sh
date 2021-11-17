#!/bin/bash

az vm start --resource-group=jepsen --name=n1 && \
    az vm start --resource-group=jepsen --name=n2 && \
    az vm start --resource-group=jepsen --name=n3 \
