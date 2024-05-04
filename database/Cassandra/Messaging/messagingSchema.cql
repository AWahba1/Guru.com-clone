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
    user_id_with_conversation UUID,
    user_name TEXT,
    user_id_with_conversation_name TEXT,
    lastEdited TIMESTAMP,
    PRIMARY KEY (user_id, lastEdited, conversation_id)
) WITH CLUSTERING ORDER BY (lastEdited DESC);



