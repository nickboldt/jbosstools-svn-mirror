<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>

<html>
<head>
</head>
<body>

<f:view>
	<h1><h:outputText value="message" /></h1>

	<h:inputText required="true" id="name"/>

	<h:message for="name" id="message" />

</f:view>
</body>
</html>