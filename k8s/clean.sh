kubectl delete namespace kube-prometheus-stack
kubectl delete clusterrole kube-prometheus-stack-grafana-clusterrole
kubectl delete clusterrole kube-prometheus-stack-kube-state-metrics
kubectl delete clusterrole kube-prometheus-stack-operator
kubectl delete clusterrole kube-prometheus-stack-prometheus
kubectl delete clusterrolebinding kube-prometheus-stack-grafana-clusterrolebinding
kubectl delete clusterrolebinding kube-prometheus-stack-kube-state-metrics
kubectl delete clusterrolebinding kube-prometheus-stack-operator
kubectl delete clusterrolebinding kube-prometheus-stack-prometheus
kubectl delete service kube-prometheus-stack-coredns -n kube-system
kubectl delete service kube-prometheus-stack-kube-controller-manager -n kube-system
kubectl delete service kube-prometheus-stack-kube-etcd -n kube-system
kubectl delete service kube-prometheus-stack-kube-proxy -n kube-system
kubectl delete service kube-prometheus-stack-kube-scheduler -n kube-system
kubectl delete mutatingwebhookconfiguration kube-prometheus-stack-admission
kubectl delete validatingwebhookconfiguration kube-prometheus-stack-admission