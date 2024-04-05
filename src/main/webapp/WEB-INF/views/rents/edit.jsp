<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<%@include file="/WEB-INF/views/common/head.jsp" %>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <%@ include file="/WEB-INF/views/common/header.jsp" %>
    <!-- Left side column. contains the logo and sidebar -->
    <%@ include file="/WEB-INF/views/common/sidebar.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                Reservations
            </h1>
        </section>

        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-12">
                    <!-- Horizontal Form -->
                    <div class="box">
                        <!-- form start -->
                        <form class="form-horizontal" method="post"
                              action="${pageContext.request.contextPath}/rents/edit?id=${id}">
                            <div class="box-body">
                                <div class="form-group">
                                    <label for="vehicle" class="col-sm-2 control-label">Voiture</label>

                                    <div class="col-sm-10">
                                        <select class="form-control" id="vehicle" name="vehicle">
                                            <jsp:useBean id="vehicles" scope="request" type="java.util.List"/>
                                            <c:forEach items="${vehicles}" var="car">
                                                <c:choose>
                                                    <c:when test="${car.id() == reservation.vehicleId()}">
                                                        <option value="${car.id()}" selected>${car.constructeur()} ${car.modele()}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${car.id()}">${car.constructeur()} ${car.modele()}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="client" class="col-sm-2 control-label">Client</label>

                                    <div class="col-sm-10">
                                        <select class="form-control" id="client" name="client">
                                            <jsp:useBean id="clients" scope="request" type="java.util.List"/>
                                            <c:forEach items="${clients}" var="client">
                                                <c:choose>
                                                    <c:when test="${client.id() == reservation.clientId()}">
                                                        <option value="${client.id()}" selected>${client.nom()} ${client.prenom()}</option>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <option value="${client.id()}">${client.nom()} ${client.prenom()}</option>
                                                    </c:otherwise>
                                                </c:choose>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="begin" class="col-sm-2 control-label">Date de debut</label>

                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="begin" name="begin" required
                                               value="<fmt:formatDate value="${reservationDebut}" pattern="dd/MM/yyyy" />"
                                               data-inputmask="'alias': 'dd/mm/yyyy'" data-mask>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="end" class="col-sm-2 control-label">Date de fin</label>
                                    <div class="col-sm-10">
                                        <input type="text" class="form-control" id="end" name="end" required
                                               value="<fmt:formatDate value="${reservationFin}" pattern="dd/MM/yyyy" />"
                                               data-inputmask="'alias': 'dd/mm/yyyy'" data-mask>
                                    </div>
                                </div>
                                <% if (request.getAttribute("error") != null) { %>
                                    <p class="error"><%= request.getAttribute("error") %></p>
                                <% } %>
                            </div>
                            <!-- /.box-body -->
                            <div class="box-footer">
                                <button type="submit" class="btn btn-info pull-right">Valider</button>
                            </div>
                            <!-- /.box-footer -->
                        </form>
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
        </section>
        <!-- /.content -->
    </div>

    <%@ include file="/WEB-INF/views/common/footer.jsp" %>
</div>
<!-- ./wrapper -->

<%@ include file="/WEB-INF/views/common/js_imports.jsp" %>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.date.extensions.js"></script>
<script src="${pageContext.request.contextPath}/resources/plugins/input-mask/jquery.inputmask.extensions.js"></script>
<script>
  $(function () {
    $('[data-mask]').inputmask()
  });
</script>
<style>
    .error {
        color: red;
    }
</style>
</body>
</html>