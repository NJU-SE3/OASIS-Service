pull:
	kubeadm config images pull --image-repository=registry.aliyuncs.com/google_containers

init:
	kubeadm init \
    --apiserver-advertise-address=101.37.116.201 \
    --pod-network-cidr=10.244.0.0/16 \
    --ignore-preflight-errors=NumCPU \
    --image-repository=registry.aliyuncs.com/google_containers\
    --v=5

install_k8s:
	bash k8s.sh

