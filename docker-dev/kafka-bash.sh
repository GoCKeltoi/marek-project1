docker-compose exec kafka bash -c 'cd $KAFKA_HOME/bin; ls; exec "${SHELL:-sh}"'