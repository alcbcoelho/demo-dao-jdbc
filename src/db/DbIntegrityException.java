package db;

/*

Essa é uma exceção que fizemos especificamente para tratar eventuais falhas de integridade referencial*.

*:

Quando, num dado BD, se apaga um registro de uma tabela que é referenciado por meio de chave estrangeira
numa outra tabela, de modo que a referência é, então, perdida (neste caso, ao se apagar um departamento específico
(e.g. 2) da tabela departamento, e os vendedores vinculados a esse departamento ficarão sem referência).

 */

public class DbIntegrityException extends RuntimeException {
    public DbIntegrityException(String msg) {
        super(msg);
    }
}
