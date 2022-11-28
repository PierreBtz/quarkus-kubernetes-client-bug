package eu.pierrebeitz.cmd;

import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.quarkus.logging.Log;
import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

import java.io.IOException;
import java.net.URL;
import java.util.List;

@TopCommand
@CommandLine.Command(name = "bug", mixinStandardHelpOptions = true)
public class EntryCommand implements Runnable {

    private final KubernetesClient kubernetesClient;

    public EntryCommand(KubernetesClient kubernetesClient) {
        this.kubernetesClient = kubernetesClient;
    }

    @Override
    public void run() {
        // will load a valid resource yaml from https://github.com/kubernetes/ingress-nginx
        try {
            var resources = kubernetesClient.load(new URL("https://raw.githubusercontent.com/kubernetes/ingress-nginx/5f2a79495a65c57106d8fab00bc95a029c3a8aab/deploy/static/provider/kind/deploy.yaml").openStream()).get();
            Log.infof("Resource Loaded: %s", resources);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}