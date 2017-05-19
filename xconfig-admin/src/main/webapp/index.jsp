<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>上传图片</title>
</head>
<body>
<form action="/xconfig/prop/upload.action" method="post" enctype="multipart/form-data">
    <input type="file" name="file" />
    <input type="text" name="token"/>
    <input type="submit" value="Submit" /></form>
</body>
</html>