<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!doctype html>
<html>
<head>
  <meta charset="utf-8" />
  <title>Create service</title>
  <style>body{font-family:Segoe UI, Tahoma, Arial;max-width:900px;margin:20px} label{display:block;margin-top:8px} input, textarea, select{width:100%;padding:6px;margin-top:4px}</style>
</head>
<body>
  <h2>Create Service</h2>
  <form action="<c:url value='/services/create'/>" method="post">
    <label>Title
      <input name="title" value="${service.title}" />
    </label>
    <label>Description
      <textarea name="description">${service.description}</textarea>
    </label>
    <label>Service Type
      <select name="serviceType">
        <option value="DAILY">DAILY</option>
        <option value="HOURLY">HOURLY</option>
      </select>
    </label>
    <label>Duration minutes
      <input name="durationMinutes" type="number" value="60" />
    </label>
    <label>Base price
      <input name="basePrice" type="number" value="1500" />
    </label>
    <label>Min capacity
      <input name="minCapacity" type="number" value="1" />
    </label>
    <label>Max capacity
      <input name="maxCapacity" type="number" value="4" />
    </label>
    <label>Active
      <select name="active"><option value="true">true</option><option value="false">false</option></select>
    </label>
    <label>Dynamic pricing
      <select name="dynamicPricing"><option value="false">false</option><option value="true">true</option></select>
    </label>
    <div style="margin-top:8px"><button type="submit">Create</button> <a href="<c:url value='/services'/>">Cancel</a></div>
  </form>
</body>
</html>
