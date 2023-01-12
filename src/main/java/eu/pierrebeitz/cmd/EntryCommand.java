package eu.pierrebeitz.cmd;

import io.fabric8.kubernetes.api.model.admissionregistration.v1.ValidatingWebhookConfigurationBuilder;
import io.fabric8.kubernetes.api.model.apps.DeploymentBuilder;
import io.fabric8.kubernetes.api.model.batch.v1.JobBuilder;
import io.fabric8.kubernetes.api.model.networking.v1.IngressClassBuilder;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.ClusterRoleBuilder;
import io.fabric8.kubernetes.api.model.rbac.RoleBindingBuilder;
import io.fabric8.kubernetes.api.model.rbac.RoleBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.annotations.RegisterForReflection;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URL;

@TopCommand
@CommandLine.Command(name = "bug", mixinStandardHelpOptions = true)
// uncomment me to make the example work
//@RegisterForReflection(targets = {
//        RoleBuilder.class,
//        ClusterRoleBuilder.class,
//        RoleBindingBuilder.class,
//        ClusterRoleBindingBuilder.class,
//        DeploymentBuilder.class,
//        JobBuilder.class,
//        IngressClassBuilder.class,
//        ValidatingWebhookConfigurationBuilder.class
//})
public class EntryCommand implements Runnable {

    private final KubernetesClient kubernetesClient;

    public EntryCommand(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void run() {
        // will load a valid resource yaml from https://github.com/kubernetes/ingress-nginx
        // CAREFUL, this WILL ALSO deploy ingress-nginx
        try {
            var resources = kubernetesClient.load(new URL("https://raw.githubusercontent.com/kubernetes/ingress-nginx/5f2a79495a65c57106d8fab00bc95a029c3a8aab/deploy/static/provider/kind/deploy.yaml").openStream()).get();
            kubernetesClient.resourceList(resources).createOrReplace();
            Log.infof("Resource Loaded: %s", resources);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}