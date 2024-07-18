package dependecies

interface MyRepository {
    fun helloWorld(): String
}

class MyRepositoryImpl(
    private val dbClient: DbClient,
) : MyRepository {
    override fun helloWorld(): String = "Hello World from RepoDb by ${dbClient.getDataFromDb()}"
}
