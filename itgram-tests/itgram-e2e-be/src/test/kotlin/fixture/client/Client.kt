package fixture.client

interface Client {
    /**
     * @param version версия АПИ (v1)
     * @param path путь к ресурсу, имя топика и т.п.
     * @param request тело сообщения в виде строки
     * @return тело ответа
     */
    suspend fun sendAndReceive(version: String, path: String, request: String): String
}