FROM itzg/minecraft-server:java8
ENV VERSION="1.8.8-R0.1-SNAPSHOT-latest"
ENV TYPE="SPIGOT"
ENV EULA="TRUE"
COPY target/CombatLog-1.0-SNAPSHOT.jar /plugins/CombatLog-1.0-SNAPSHOT.jar
EXPOSE 25565:25565