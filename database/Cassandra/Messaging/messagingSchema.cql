CREATE KEYSPACE my_keyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE my_keyspace;

CREATE TABLE conversations (
    conversation_id UUID PRIMARY KEY,
    user1_id UUID,
    user1_name TEXT, 
    user2_id UUID,
    user2_name TEXT,
    created_at TIMESTAMP
);

CREATE TABLE messages (
    conversation_id UUID,
    message_id TIMEUUID,
    sender_id UUID,
    sender_name TEXT,
    receiver_id UUID,
    receiver_name TEXT,
    sent_at TIMESTAMP,
    message_text TEXT,
    PRIMARY KEY (conversation_id, sent_at, message_id)
) WITH CLUSTERING ORDER BY (sent_at DESC);




