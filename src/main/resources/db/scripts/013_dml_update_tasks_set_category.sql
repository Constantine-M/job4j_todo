UPDATE tasks SET category_id = (SELECT id FROM categories WHERE name = 'uncategorized');