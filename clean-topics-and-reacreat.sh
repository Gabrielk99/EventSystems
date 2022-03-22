kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic vacina
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic gestor
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic frontend-vaccine
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic alertas
kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic notificacao

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic vacina --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic gestor --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic frontend-vaccine --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic alertas --partitions 3 --replication-factor 1

kafka-topics.sh --bootstrap-server localhost:9092 --create --topic notificacao --partitions 3 --replication-factor 1