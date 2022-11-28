# quarkus-kubernetes-client-bug Project

This is a reproduction for https://github.com/quarkusio/quarkus/issues/29532
It will inject a `KubernetesClient` with the default configuration and try to load some Kubernetes resources.

Steps:

* use Graalvm 22.3.0
* clone this repository
* run the command as a jar: `mvn quarkus:dev`, observe that the resources are loaded
* build a native binary (`mvn verify -Pnative`) and run it `./target/quarkus-kubernetes-client-bug-1.0.0-SNAPSHOT-runner`, observe the failure:

```text
io.fabric8.kubernetes.client.KubernetesClientException: Cannot edit io.fabric8.kubernetes.api.model.apps.Deployment with visitors, no builder was found
	at io.fabric8.kubernetes.client.impl.ResourceHandlerImpl.edit(ResourceHandlerImpl.java:61)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.acceptVisitors(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:292)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.lambda$getItems$1(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:112)
	at java.base@17.0.5/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197)
	at java.base@17.0.5/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625)
	at java.base@17.0.5/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509)
	at java.base@17.0.5/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499)
	at java.base@17.0.5/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921)
	at java.base@17.0.5/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234)
	at java.base@17.0.5/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.getItems(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:114)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.resources(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:119)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.getResources(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:124)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.performOperation(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:327)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.get(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:224)
	at io.fabric8.kubernetes.client.dsl.internal.NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.get(NamespaceVisitFromServerGetWatchDeleteRecreateWaitApplicableListImpl.java:62)
	at eu.pierrebeitz.cmd.EntryCommand.run(EntryCommand.java:27)
	at picocli.CommandLine.executeUserObject(CommandLine.java:1939)
	at picocli.CommandLine.access$1300(CommandLine.java:145)
	at picocli.CommandLine$RunLast.executeUserObjectOfLastSubcommandWithSameParent(CommandLine.java:2358)
	at picocli.CommandLine$RunLast.handle(CommandLine.java:2352)
	at picocli.CommandLine$RunLast.handle(CommandLine.java:2314)
	at picocli.CommandLine$AbstractParseResultHandler.execute(CommandLine.java:2179)
	at picocli.CommandLine$RunLast.execute(CommandLine.java:2316)
	at io.quarkus.picocli.runtime.PicocliRunner$EventExecutionStrategy.execute(PicocliRunner.java:26)
	at picocli.CommandLine.execute(CommandLine.java:2078)
	at io.quarkus.picocli.runtime.PicocliRunner.run(PicocliRunner.java:40)
	at io.quarkus.runtime.ApplicationLifecycleManager.run(ApplicationLifecycleManager.java:131)
	at io.quarkus.runtime.Quarkus.run(Quarkus.java:70)
	at io.quarkus.runtime.Quarkus.run(Quarkus.java:43)
	at io.quarkus.runner.GeneratedMain.main(Unknown Source)
```

NOTE: There is no side effect on whatever cluster in your current context, the resources are only loaded.

You can reproduce the issue on `2.14.1.Final` (eg: `mvn verify -Pnative -D`), but it is not present in `2.13.5.Final`

----------

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/quarkus-kubernetes-client-bug-1.0.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.

## Related Guides

- Kubernetes Client ([guide](https://quarkus.io/guides/kubernetes-client)): Interact with Kubernetes and develop Kubernetes Operators
