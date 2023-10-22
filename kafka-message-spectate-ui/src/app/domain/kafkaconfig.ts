export class Config {
    host: String;
    topics: Array<String>;
    consumerGroups: Array<String>;
}

export class KafkaConfig{
    config: Array<Config>;
}