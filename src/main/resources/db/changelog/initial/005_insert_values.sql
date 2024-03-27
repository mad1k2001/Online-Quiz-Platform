INSERT INTO users (name, email, password, enabled)
VALUES
    ('John Doe', 'john.doe@example.com', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', true),
    ('Alice Smith', 'alice.smith@example.com', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', true),
    ('Bob Johnson', 'bob.johnson@example.com', '$2a$12$WB2YUbFcCN0tm44SBcKUjua9yiFBsfB3vW02IjuwzY7HGtlQIKzy2', true);

INSERT INTO quizzes (title, description, creator_id)
VALUES
    ('History Quiz', 'Test your knowledge of historical events', (SELECT id FROM users WHERE email = 'john.doe@example.com')),
    ('Science Quiz', 'Explore scientific discoveries and theories', (SELECT id FROM users WHERE email = 'alice.smith@example.com')),
    ('Literature Quiz', 'Discover famous literary works and authors', (SELECT id FROM users WHERE email = 'bob.johnson@example.com'));

INSERT INTO questions (question_text, quiz_id)
VALUES
    ('What year did World War II end?', (SELECT id FROM quizzes WHERE title = 'History Quiz')),
    ('Who developed the theory of relativity?', (SELECT id FROM quizzes WHERE title = 'Science Quiz')),
    ('Who wrote "To Kill a Mockingbird"?', (SELECT id FROM quizzes WHERE title = 'Literature Quiz'));

INSERT INTO options (option_text, is_correct, question_id)
VALUES
    ('1945', true, (SELECT id FROM questions WHERE question_text = 'What year did World War II end?')),
    ('1950', false, (SELECT id FROM questions WHERE question_text = 'What year did World War II end?')),
    ('1960', false, (SELECT id FROM questions WHERE question_text = 'What year did World War II end?')),
    ('Albert Einstein', true, (SELECT id FROM questions WHERE question_text = 'Who developed the theory of relativity?')),
    ('Isaac Newton', false, (SELECT id FROM questions WHERE question_text = 'Who developed the theory of relativity?')),
    ('Stephen Hawking', false, (SELECT id FROM questions WHERE question_text = 'Who developed the theory of relativity?')),
    ('Harper Lee', true, (SELECT id FROM questions WHERE question_text = 'Who wrote "To Kill a Mockingbird"?')),
    ('Ernest Hemingway', false, (SELECT id FROM questions WHERE question_text = 'Who wrote "To Kill a Mockingbird"?')),
    ('Mark Twain', false, (SELECT id FROM questions WHERE question_text = 'Who wrote "To Kill a Mockingbird"?'));

INSERT INTO quiz_results (score, quiz_id, user_id)
VALUES
    (80.0, (SELECT id FROM quizzes WHERE title = 'History Quiz'), (SELECT id FROM users WHERE email = 'john.doe@example.com')),
    (90.0, (SELECT id FROM quizzes WHERE title = 'Science Quiz'), (SELECT id FROM users WHERE email = 'alice.smith@example.com')),
    (70.0, (SELECT id FROM quizzes WHERE title = 'Literature Quiz'), (SELECT id FROM users WHERE email = 'bob.johnson@example.com'));

INSERT INTO authorities (role)
VALUES ('ADMIN'),
    ('GUEST');

INSERT INTO user_authorities (user_id, authority_id)
VALUES
    ((SELECT id FROM users WHERE email = 'john.doe@example.com'), (SELECT id FROM authorities WHERE role = 'ADMIN')),
    ((SELECT id FROM users WHERE email = 'alice.smith@example.com'), (SELECT id FROM authorities WHERE role = 'GUEST')),
    ((SELECT id FROM users WHERE email = 'bob.johnson@example.com'), (SELECT id FROM authorities WHERE role = 'GUEST'));