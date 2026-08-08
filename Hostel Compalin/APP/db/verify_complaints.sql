-- Verify the seeded complaints records
SELECT complaint_id, room, category, priority, status
FROM complaints
ORDER BY complaint_id;
