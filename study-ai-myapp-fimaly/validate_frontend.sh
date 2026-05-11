#!/bin/bash

echo "=== 前端功能验证报告 ==="
echo ""
echo "1. 页面访问验证"
echo "-------------------"

pages=("/login" "/register" "/home" "/family-management" "/family-tree" "/members" "/member/1" "/events" "/media" "/settings" "/relationships" "/member-search" "/progress" "/milestones" "/location-map" "/ai-relationship-analysis" "/image-import-analysis" "/operation-logs" "/family-stories" "/profile")

for page in "${pages[@]}"
do
    http_status=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:5173$page")
    if [ "$http_status" -eq 200 ]; then
        echo "✅ $page - 页面可访问"
    else
        echo "❌ $page - 页面访问失败 (HTTP $http_status)"
    fi
done

echo ""
echo "2. 登录功能验证"
echo "-------------------"

login_result=$(curl -s -X POST http://localhost:8080/api/auth/login \
    -H "Content-Type: application/json" \
    -d '{"email":"bage@qq.com","password":"bage1234"}')

if echo "$login_result" | grep -q "token"; then
    echo "✅ 登录功能正常 - 成功获取token"
    TOKEN=$(echo "$login_result" | sed 's/.*"token":"\([^"]*\)".*/\1/')
else
    echo "❌ 登录功能异常: $login_result"
    exit 1
fi

echo ""
echo "3. 注册功能验证"
echo "-------------------"

register_email="test_$(date +%s)@example.com"
register_result=$(curl -s -X POST http://localhost:8080/api/auth/register \
    -H "Content-Type: application/json" \
    -d "{\"email\":\"$register_email\",\"password\":\"password123\",\"nickname\":\"测试用户\"}")

if echo "$register_result" | grep -q "id"; then
    echo "✅ 注册功能正常 - 用户创建成功"
else
    echo "❌ 注册功能异常: $register_result"
fi

echo ""
echo "4. 获取家族列表验证（已认证）"
echo "-------------------"

family_list=$(curl -s -X GET http://localhost:8080/api/families \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$family_list" | grep -q "name"; then
    family_count=$(echo "$family_list" | grep -o '"name"' | wc -l)
    echo "✅ 获取家族列表正常 - 共 $family_count 个家族"
else
    echo "❌ 获取家族列表异常: $family_list"
fi

echo ""
echo "5. 获取成员列表验证（已认证）"
echo "-------------------"

member_list=$(curl -s -X GET "http://localhost:8080/api/members?familyId=1" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$member_list" | grep -q "name"; then
    member_count=$(echo "$member_list" | grep -o '"name"' | wc -l)
    echo "✅ 获取成员列表正常 - 共 $member_count 个成员"
else
    echo "❌ 获取成员列表异常: $member_list"
fi

echo ""
echo "6. 获取关系列表验证（已认证）"
echo "-------------------"

relation_list=$(curl -s -X GET "http://localhost:8080/api/relationships?familyId=1" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$relation_list" | grep -q "relationshipType"; then
    relation_count=$(echo "$relation_list" | grep -o '"relationshipType"' | wc -l)
    echo "✅ 获取关系列表正常 - 共 $relation_count 个关系"
else
    echo "❌ 获取关系列表异常: $relation_list"
fi

echo ""
echo "7. 获取事件列表验证（已认证）"
echo "-------------------"

event_list=$(curl -s -X GET "http://localhost:8080/api/events?familyId=1" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$event_list" | grep -q "name"; then
    event_count=$(echo "$event_list" | grep -o '"name"' | wc -l)
    echo "✅ 获取事件列表正常 - 共 $event_count 个事件"
else
    echo "❌ 获取事件列表异常: $event_list"
fi

echo ""
echo "8. 获取媒体列表验证（已认证）"
echo "-------------------"

media_list=$(curl -s -X GET "http://localhost:8080/api/media?familyId=1" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$media_list" | grep -q "url"; then
    media_count=$(echo "$media_list" | grep -o '"url"' | wc -l)
    echo "✅ 获取媒体列表正常 - 共 $media_count 个媒体文件"
else
    echo "❌ 获取媒体列表异常: $media_list"
fi

echo ""
echo "9. 获取操作日志验证（已认证）"
echo "-------------------"

log_list=$(curl -s -X GET "http://localhost:8080/api/operation-logs" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$log_list" | grep -q "operationType"; then
    log_count=$(echo "$log_list" | grep -o '"operationType"' | wc -l)
    echo "✅ 获取操作日志正常 - 共 $log_count 条日志"
else
    echo "❌ 获取操作日志异常: $log_list"
fi

echo ""
echo "10. 获取个人信息验证（已认证）"
echo "-------------------"

profile_result=$(curl -s -X GET "http://localhost:8080/api/profile" \
    -H "Content-Type: application/json" \
    -H "Authorization: Bearer $TOKEN")

if echo "$profile_result" | grep -q "email"; then
    echo "✅ 获取个人信息正常"
else
    echo "❌ 获取个人信息异常: $profile_result"
fi

echo ""
echo "=== 验证完成 ==="