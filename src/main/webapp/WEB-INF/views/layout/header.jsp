<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="<c:url value="/resources/js/util/header.js"/>"></script>
<!-- Navigation-->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top" id="mainNav">
    <a href="<c:url value="/all/dash"/>"><img src="<c:url value="/resources/images/logo_dos.png"/>" /></a>

    <button class="navbar-toggler navbar-toggler-right" type="button" data-toggle="collapse" data-target="#navbarResponsive" 
            aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarResponsive">
        <ul class="navbar-nav navbar-sidenav" id="exampleAccordion">
            <c:forEach items="${menuSession}" var="menuItem">
                <li class="nav-item" data-toggle="tooltip" data-placement="right" title="${menuItem.name}">
                    <a class="nav-link" href="<c:url value="${menuItem.url}"/>">
                        <i class="fa fa-fw ${menuItem.icon}"></i>
                        <span class="nav-link-text">${menuItem.name}</span>
                    </a>
                </li>
            </c:forEach>
        </ul>
        <ul class="navbar-nav sidenav-toggler">
            <li class="nav-item">
                <a class="nav-link text-center" id="sidenavToggler">
                    <i class="fa fa-fw fa-angle-left"></i>
                </a>
            </li>
        </ul>

        <ul class="navbar-nav ml-auto">

            <li class="nav-item dropdown">
                <div class="btn-group">
                    <button type="button" class="btn btn-danger" id="btnDash">
                        <i class="fa fa-fw fa-th"></i>
                    </button>
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        <i class="fa fa-fw fa-user" style="padding-right: 15px"></i>${userSession.father.username}
                    </button>
                    <div class="dropdown-menu">
                        <button class="dropdown-item" id="btnConfiguration">
                            <i class="fa fa-wrench" style="padding-right: 10px"></i>Configuración
                        </button>
                        <div class="dropdown-divider"></div>
                        <button class="dropdown-item" id="btnLogout">
                            <i class="fa fa-sign-out" style="padding-right: 10px"></i>Salir
                        </button>
                    </div>
                </div>
            </li>
        </ul>
    </div>
</nav>
