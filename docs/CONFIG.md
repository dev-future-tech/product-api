# Configuration

# Install RabbitMQ

```
$ brew install rabbitmq
```

Edit the RabbitMQ Config file:


```
NODENAME=rabbit@localhost
RABBITMQ_LOG_BASE=/usr/local/var/log/rabbitmq
CONFIG_FILE=/Users/anthonyikeda/datastores/rabbitmq/rabbitmq.conf
```
*/usr/local/etc/rabbitmq/rabbitmq-env.conf*


Set up your RabbitMQ Configuration:

```
listeners.tcp.1 = 0.0.0.0:5672
```
*/Users/anthonyikeda/datastores/rabbitmq/rabbitmq.conf*

