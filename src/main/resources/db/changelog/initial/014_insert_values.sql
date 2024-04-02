INSERT INTO categories (name)
VALUES
    ('History'),
    ('Science'),
    ('Literature');

UPDATE quizzes
SET category_id = (SELECT id FROM categories WHERE name = 'History')
WHERE title = 'History Quiz';

UPDATE quizzes
SET category_id = (SELECT id FROM categories WHERE name = 'Science')
WHERE title = 'Science Quiz';

UPDATE quizzes
SET category_id = (SELECT id FROM categories WHERE name = 'Literature')
WHERE title = 'Literature Quiz';

UPDATE quiz_results
SET quiz_rating = 80
WHERE id = 1;

UPDATE quiz_results
SET quiz_rating = 90
WHERE id = 2;

UPDATE quiz_results
SET quiz_rating = 70
WHERE id = 3;

UPDATE quiz_results
SET correct_answers = 8,
    total_questions = 10
WHERE id = 1;

UPDATE quiz_results
SET correct_answers = 9,
    total_questions = 10
WHERE id = 2;

UPDATE quiz_results
SET correct_answers = 7,
    total_questions = 10
WHERE id = 3;
