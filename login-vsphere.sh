#!/bin/bash
# William Lam
# www.virtuallyghetto.com

VSPHERE_WITH_TANZU_CONTROL_PLANE_IP=$1
VSPHERE_WITH_TANZU_USERNAME=$2
VSPHERE_WITH_TANZU_PASSWORD=$3
VSPHERE_WITH_TANZU_NAMESPACE=$4
VSPHERE_WITH_TANZU_CLUSTER=$5

KUBECTL_VSPHERE_PATH=/Users/lamw/Desktop/bin/kubectl-vsphere
KUBECTL_PATH=/usr/local/bin/kubectl

KUBECTL_VSPHERE_LOGIN_COMMAND=$(expect -c "
spawn $KUBECTL_VSPHERE_PATH login --server=$VSPHERE_WITH_TANZU_CONTROL_PLANE_IP --vsphere-username $VSPHERE_WITH_TANZU_USERNAME --insecure-skip-tls-verify \
--tanzu-kubernetes-cluster-namespace $VSPHERE_WITH_TANZU_NAMESPACE --tanzu-kubernetes-cluster-name $VSPHERE_WITH_TANZU_CLUSTER
expect \"*?assword:*\"
send -- \"$VSPHERE_WITH_TANZU_PASSWORD\r\"
expect eof
")

${KUBECTL_PATH} config use-context ${VSPHERE_WITH_TANZU_NAMESPACE}
