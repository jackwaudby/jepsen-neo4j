#!/bin/bash

az vm deallocate --resource-group=jepsen --name=n1 && \
    az vm deallocate --resource-group=jepsen --name=n2 && \
    az vm deallocate --resource-group=jepsen --name=n3 \
