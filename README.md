# Kafka

Commands:
* Start Zookeeper: bin/zookeeper-server-start config/zookeeper.properties
* Start Kafka:  bin/kafka-server-start config/server.properties
* Create topic: bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 partitions 1 --topic LOJA_NOVO_PEDIDO
* List topics: bin/kafka-topics --list --bootstrap-server localhost:9092
* Produce messages: bin/kafka-console-producer --broker-list localhost:9092 --topic LOJA_NOVO_PEDIDO ( everything you type will be a message to the topic
* Consume messages: bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic LOJA_NOVO_PEDIDO --from-beginning ( the last command will read since the beginning, if not passed It will read only new messages )

Tips
Todos os comandos são feitos dentro da pasta do Kafka
A pasta em que o Kafka se encontra, não pode ter espaços se não da tilt
O Kafka so vai funcionar se o zookeeper estiver de pé
