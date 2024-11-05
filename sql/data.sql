# INSERT INTO user (user_account, user_password, user_name, user_role, email, phone_number)
# VALUES
#     ('user1', '3c71cc041a828d5ee12891ee3e637cc5', '张三', 'user', 'zhangsan1@example.com', '13800000001'),
#     ('user2', '3c71cc041a828d5ee12891ee3e637cc5', '李四', 'user', 'lisi2@example.com', '13800000002'),
#     ('admin1', '3c71cc041a828d5ee12891ee3e637cc5', '管理员', 'user', 'admin1@example.com', '13800000003'),
#     ('user3', '3c71cc041a828d5ee12891ee3e637cc5', '王五', 'user', 'wangwu3@example.com', '13800000004'),
#     ('user4', '3c71cc041a828d5ee12891ee3e637cc5', '赵六', 'user', 'zhaoliu4@example.com', '13800000005'),
#     ('user5', '3c71cc041a828d5ee12891ee3e637cc5', '孙七', 'user', 'sunqi5@example.com', '13800000006'),
#     ('admin2', '3c71cc041a828d5ee12891ee3e637cc5', '超级管理员', 'user', 'admin2@example.com', '13800000007'),
#     ('user6', '3c71cc041a828d5ee12891ee3e637cc5', '周八', 'user', 'zhouba6@example.com', '13800000008'),
#     ('user7', '3c71cc041a828d5ee12891ee3e637cc5', '郑九', 'user', 'zhengjiu7@example.com', '13800000009'),
#     ('user8', '3c71cc041a828d5ee12891ee3e637cc5', '钱十', 'user', 'qianshi8@example.com', '13800000010');

# INSERT INTO category (name, user_id)
# VALUES
#     ('前端开发', 1),
#     ('后端开发', 2),
#     ('移动开发', 3),
#     ('数据科学', 4),
#     ('人工智能', 5),
#     ('机器学习', 6),
#     ('大数据', 7),
#     ('云计算', 8),
#     ('区块链', 9),
#     ('编程语言', 10);
#
# INSERT INTO tag (name, user_id)
# VALUES
#     ('JavaScript', 1),
#     ('Python', 2),
#     ('Java', 3),
#     ('React', 4),
#     ('Django', 5),
#     ('TensorFlow', 6),
#     ('Hadoop', 7),
#     ('AWS', 8),
#     ('Solidity', 9),
#     ('C++', 10);

INSERT INTO post (title, content, category_id, tags, thumb_num, favour_num, view_count, is_draft, user_id)
VALUES
    ('深入学习前端开发技术', '本篇文章将深入探讨现代前端开发技术，包括框架、工具链和开发流程。', 1, '["JavaScript", "React"]', 0, 0, 0, '0', 1),
    ('后端开发的最佳实践', '后端开发是构建高效系统的关键，本文将介绍一些常见的后端开发最佳实践。', 2, '["Java", "Django"]', 0, 0, 0, '0', 2),
    ('移动开发的趋势与技术', '随着移动设备的普及，移动开发变得越来越重要，本文将讨论当前的移动开发技术。', 3, '["Python", "React"]', 0, 0, 0, '0', 3),
    ('数据科学基础', '数据科学已经成为现代科技领域的热点，本文将介绍数据科学的基础知识和技术。', 4, '["Python", "TensorFlow"]', 0, 0, 0, '1', 1),
    ('人工智能的未来', '人工智能正在快速发展，本文探讨了人工智能技术的最新进展和未来的趋势。', 5, '["Python", "TensorFlow"]', 0, 0, 0, '0', 5),
    ('机器学习入门', '机器学习是人工智能的重要分支，本文将介绍机器学习的基础概念和实践技巧。', 6, '["Python", "TensorFlow"]', 0, 0, 0, '0', 6),
    ('大数据与云计算', '大数据和云计算是现代计算机技术的重要组成部分，本文将介绍这两者如何协同工作。', 7, '["Hadoop", "AWS"]', 0, 0, 0, '1', 1),
    ('区块链技术简介', '区块链是一项革命性的技术，本文将简要介绍区块链的工作原理以及应用场景。', 8, '["Solidity", "AWS"]', 0, 0, 0, '0', 8),
    ('编程语言的演变', '编程语言经历了数十年的演变，本文将讨论一些重要编程语言的历史和现状。', 9, '["C++", "JavaScript"]', 0, 0, 0, '0', 1),
    ('如何使用JavaScript进行前端开发', 'JavaScript 是前端开发的核心语言，本文将介绍如何使用 JavaScript 开发现代网站。', 1, '["JavaScript", "React"]', 0, 0, 0, '0', 10);

