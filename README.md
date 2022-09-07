# Kafka

Commands:
* Start Zookeeper: bin/zookeeper-server-start config/zookeeper.properties
* Start Kafka:  bin/kafka-server-start config/server.properties
* Create topic: bin/kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 partitions 1 --topic LOJA_NOVO_PEDIDO
* List topics: bin/kafka-topics --list --bootstrap-server localhost:9092
* Produce messages: bin/kafka-console-producer --broker-list localhost:9092 --topic ECOMMERCE_NEW_ORDER ( everything you type will be a message to the topic
* Consume messages: bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic ECOMMERCE_NEW_ORDER --from-beginning ( the last command will read since the beginning, if not passed It will read only new messages )
* Alter topic properties: bin/kafka-topics.sh --alter --zookeeper localhost:2181 --topic ECOMMERCE_NEW_ORDER --partitions 2

Tips
The Kafka folder should not have spaces in the name 
Kafka will only work if we have a zookeeper running
Any alteration in the config properties will affect ONLY new topics
They way kafka decides which topic to use when it has multiples one are defined by the "key" value of the record