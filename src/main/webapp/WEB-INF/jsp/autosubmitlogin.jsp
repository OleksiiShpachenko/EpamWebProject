<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form  action="pages" id="some_id" name="loginFormAutoSubmit" method=POST style="display: none;">
    <input type="hidden" name="command" value="login" /> 
		<input type="hidden" name="login" value="" />
		<input type="hidden" name="password" value="" /> 
		<input type="submit" value="Submit!">
</form>
<script type="text/javascript">
window.onload = function() {
    document.forms["loginFormAutoSubmit"].submit();
}
</script>
</body>
</html>
