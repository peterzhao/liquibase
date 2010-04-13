package liquibase.sqlgenerator.core;

import liquibase.database.Database;
import liquibase.database.core.SybaseDatabase;
import liquibase.database.typeconversion.TypeConverterFactory;
import liquibase.exception.ValidationErrors;
import liquibase.sql.Sql;
import liquibase.sqlgenerator.SqlGenerator;
import liquibase.sqlgenerator.SqlGeneratorChain;
import liquibase.sqlgenerator.SqlGeneratorFactory;
import liquibase.statement.NotNullConstraint;
import liquibase.statement.UniqueConstraint;
import liquibase.statement.core.CreateDatabaseChangeLogTableStatement;
import liquibase.statement.core.CreateTableStatement;

public class CreateDatabaseChangeLogTableGenerator implements SqlGenerator<CreateDatabaseChangeLogTableStatement> {
    public int getPriority() {
        return PRIORITY_DEFAULT;
    }

    public boolean supports(CreateDatabaseChangeLogTableStatement statement, Database database) {
        return (!(database instanceof SybaseDatabase));
    }

    public ValidationErrors validate(CreateDatabaseChangeLogTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        return new ValidationErrors();
    }

    public Sql[] generateSql(CreateDatabaseChangeLogTableStatement statement, Database database, SqlGeneratorChain sqlGeneratorChain) {
        CreateTableStatement createTableStatement = new CreateTableStatement(database.getLiquibaseSchemaName(), database.getDatabaseChangeLogTableName())
                .addPrimaryKeyColumn("ID", "VARCHAR(63)", null, null, null,new NotNullConstraint())
                .addPrimaryKeyColumn("AUTHOR", "VARCHAR(63)", null, null, null,new NotNullConstraint())
                .addPrimaryKeyColumn("FILENAME", "VARCHAR(200)", null, null, null,new NotNullConstraint())
                .addColumn("DATEEXECUTED", TypeConverterFactory.getInstance().findTypeConverter(database).getDateTimeType().getDataTypeName(), null, new NotNullConstraint())
                .addColumn("ORDEREXECUTED", "INT", new NotNullConstraint(), new UniqueConstraint("UQ_DBCL_ORDEREXEC"))
                .addColumn("EXECTYPE", "VARCHAR(10)", new NotNullConstraint())
                .addColumn("MD5SUM", "VARCHAR(35)")
                .addColumn("DESCRIPTION", "VARCHAR(255)")
                .addColumn("COMMENTS", "VARCHAR(255)")
                .addColumn("TAG", "VARCHAR(255)")
                .addColumn("LIQUIBASE", "VARCHAR(20)");

        return SqlGeneratorFactory.getInstance().generateSql(createTableStatement, database);
    }
}