<#-- @ftlvariable name="" type="technology.zook.dropwizard.hazelcast.example.views.IndexPageView" -->
<html>
  <body>
    <h1>Hello, ${name}!</h1>
    <h3>What to change your name?</h3>
    <form action="/name" method="POST">
      <label for="name">Enter name:</label>
      <input type="text" name="name"/>
      <button type="submit">Submit</button>
    </form>
  </body>
</html>