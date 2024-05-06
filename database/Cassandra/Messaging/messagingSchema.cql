CREATE KEYSPACE my_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE my_keyspace;

CREATE TABLE search_conversation (
    user_id UUID,
    conversation_id UUID,
    user_with_conversation_id UUID,
    user_name TEXT,
    user_with_conversation_name TEXT,
    lastEdited TIMESTAMP,
    chat_open BOOLEAN,
    PRIMARY KEY (user_id, user_with_conversation_name, lastEdited, conversation_id)
) WITH CLUSTERING ORDER BY (user_with_conversation_name ASC, lastEdited DESC);



CREATE TABLE message (
    conversation_id UUID,
    message_id UUID,
    sender_id UUID,
    sender_name TEXT,
    receiver_id UUID,
    receiver_name TEXT,
    sent_at TIMESTAMP,
    message_text TEXT,
    PRIMARY KEY (conversation_id, sent_at, message_id)
) WITH CLUSTERING ORDER BY (sent_at DESC);

CREATE TABLE see_conversations (
    user_id UUID,
    conversation_id UUID,
    user_with_conversation_id UUID,
    user_name TEXT,
    user_with_conversation_name TEXT,
    lastEdited TIMESTAMP,
    chat_open BOOLEAN,
    PRIMARY KEY (user_id, conversation_id)
);


cd etc/cassandra/cassandra.yaml


CREATE CUSTOM INDEX search_conversation_with_conversation_name_idx
ON see_conversations(user_with_conversation_name) 
USING 'org.apache.cassandra.index.sasi.SASIIndex'
WITH OPTIONS = {
    'mode': 'CONTAINS',
    'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.StandardAnalyzer',
    'tokenization_enable_stemming': 'true',
    'tokenization_locale': 'en',
    'tokenization_skip_stop_words': 'true',
    'analyzed': 'true',
    'tokenization_normalize_lowercase': 'true'
};

CREATE INDEX lastEdited_idx ON see_conversations (lastEdited);

