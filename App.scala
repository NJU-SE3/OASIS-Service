class App extends Simulation {

  val scn = scenario("AddAndFindPersons").repeat(1000, "n") {
    exec(
      http("AddPerson-API")
        .post("http://localhost:8080/persons")
        .header("Content-Type", "application/json")
        .body(StringBody("""{"firstName":"John${n}","lastName":"Smith${n}","birthDate":"1980-01-01", "address": {"country":"pl","city":"Warsaw","street":"Test${n}","postalCode":"02-200","houseNo":${n}}}"""))
        .check(status.is(200))
    ).pause(Duration.apply(5, TimeUnit.MILLISECONDS))
  }.repeat(1000, "n") {
    exec(
      http("GetPerson-API")
        .get("http://localhost:8080/persons/${n}")
        .check(status.is(200))
    )
  }

  setUp(scn.inject(atOnceUsers(30))).maxDuration(FiniteDuration.apply(10, "minutes"))

}