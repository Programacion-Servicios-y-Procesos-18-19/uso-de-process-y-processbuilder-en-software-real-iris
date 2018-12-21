package de.metas.migration.executor.impl;

/*
 * #%L
 * de.metas.migration.base
 * %%
 * Copyright (C) 2015 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.metas.migration.IDatabase;
import de.metas.migration.IScript;
import de.metas.migration.exception.ScriptExecutionException;
import de.metas.migration.executor.IScriptExecutor;

public class PostgresqlNativeExecutor implements IScriptExecutor
{
	public static final String ENV_PG_HOME = "PG_HOME";
	public static final String ENV_PGPASSWORD = "PGPASSWORD";

	public static final String PARAM_DBHOST = "db.host";
	public static final String PARAM_DBPORT = "db.port";
	public static final String PARAM_DBNAME = "db.dbname";
	public static final String PARAM_DBUSER = "db.user";
	public static final String PARAM_DBPASSWORD = "db.password";

	private final IDatabase database;

	private final String command;
	private final List<String> args;
	private final Map<String, String> environment;

	public PostgresqlNativeExecutor(final IDatabase database)
	{
		this.database = database;

		//
		// Configure: psql command
		final String pgHome = getEnv(ENV_PG_HOME);
		if (pgHome == null || pgHome.trim().length() == 0)
		{
			command = "psql";
		}
		else
		{
			command = pgHome + File.separator + "bin" + File.separator + "psql";
		}

		//
		// Configure: psql command arguments
		{
			args = new ArrayList<String>();
			addParameter(args, "-h", database.getDbHostname());
			addParameter(args, "-p", database.getDbPort());
			addParameter(args, "-d", database.getDbName());
			addParameter(args, "-U", database.getDbUser());

			// Execute the script in one single transaction
			// see http://petereisentraut.blogspot.ro/2010/03/running-sql-scripts-with-psql.html
			args.add("--single-transaction");

			// Stop script execution on first error.
			// If this value is not set or is ZERO the script execution will continue and the exit code will be 0, no matter if one command failed
			// see http://petereisentraut.blogspot.ro/2010/03/running-sql-scripts-with-psql.html
			args.add("--set");
			args.add("ON_ERROR_STOP=1");
		}

		//
		// Configure: psql running environment
		{
			environment = new HashMap<String, String>();

			if (database.getDbPassword() != null)
			{
				// NOTE: we are setting the ENV_PGPASSWORD only if DbPassword is set
				// else we rely on other authentication methods
				// e.g. "pgpass" file
				environment.put(ENV_PGPASSWORD, database.getDbPassword());
			}
		}
	}

	@Override
	public String toString()
	{
		return getClass().getSimpleName() + " ["
				+ "command=" + command
				+ ", args=" + args
				+ ", environment=" + environment
				+ "]";
	}

	private String getEnv(final String name)
	{
		final String value = System.getenv(name);
		return value;
	}

	private static final void addParameter(final List<String> args, final String paramName, final String value)
	{
		if (value == null || value.trim().length() == 0)
		{
			return;
		}

		args.add(paramName);
		args.add(value.trim());
	}

	@Override
	public void execute(final IScript script)
	{
		/*Funcion que ejecuta el proceso hijo */
		final Process proc = startProcess(script); //inicializa el proceso

		final List<String> logTail = readOutput(proc.getInputStream(), 100);
		final int exitValue;
		try
		{
			exitValue = proc.waitFor(); // el proceso hijo se pone en espera
			//OSCAR: No es correcto: es el proceso actual (el proceso padre) el que se pone en espera hasta que termina el proceso hijo y devuelva su valor.
		}
		catch (final InterruptedException e)
		{
			throw new ScriptExecutionException("Process executor interrupted", e)
					.setDatabase(database)
					.setScript(script)
					.setExecutor(this)
					.setLog(logTail);
		}

		if (exitValue != 0)
		{
			throw new ScriptExecutionException("Script execution failed")
					.setDatabase(database)
					.setScript(script)
					.setExecutor(this)
					.addParameter("ProcessExitCode", exitValue)
					.setLog(logTail);
		}
	}

/* Funcion que crea el proceso padre*/
//OSCAR: El proceso padre ya está creado (es la aplicación principal), aquí lo que hacemos es lanzar el proceso hijo con un comando y unos argumentos.
	private Process startProcess(final IScript script)
	{
		final List<String> cmdarrayList = new ArrayList<String>();
		cmdarrayList.add(command); //OSCAR: ¿Qué contiene este comando? Lo puedes encontrar en este archivo... (command)
		cmdarrayList.addAll(args); //OSCAR: ¿Qué argumentos se le pasan al comando? Está también en este archivo...(args)
		addParameter(cmdarrayList, "-f", script.getLocalFile().getAbsolutePath());

/* crea un proceso padre*/
// OSCAR: ProcessBuilder no es un "proceso" es una clase que nos ayuda a "construir" el proceso hijo
		final ProcessBuilder processBuilder = new ProcessBuilder(cmdarrayList);
		final Map<String, String> env = processBuilder.environment();
		env.putAll(environment);

/* redirecciona un error del proceso */
// OSCAR: Lo que se redirecciona es la salida estándar de errores del proceso hijo... y a dónde la redirecciona? 
// Está indicado en el mismo comentario STAOUT -> standard out es la salida estándard, la pantalla
		processBuilder.redirectErrorStream(true); // forward STDERR to STDOUT

		final Process proc; // crea un proceso hijo que despues se devolverá
		try
		{
			proc = processBuilder.start(); // iniciar el proceso hijo
		}
		catch (final IOException e)
		{
			throw new ScriptExecutionException("Error creating executor process."
					+ "If 'psql' command was not found, try setting " + ENV_PG_HOME + " environment variable.", e)
					.setDatabase(database)
					.setScript(script)
					.setExecutor(this);
		}

		return proc; // devuelve el proceso hijo
	}

	private List<String> readOutput(final InputStream is, final int tailSize)
	{
		final List<String> tail = new ArrayList<String>(tailSize);

		final BufferedReader in = new BufferedReader(new InputStreamReader(is), 10240);
		try
		{
			String line;
			while ((line = in.readLine()) != null)
			{
				if (tail.size() >= tailSize)
				{
					tail.remove(0);
				}
				tail.add(line);

				// System.out.println("\t" + line);
			}
		}
		catch (final IOException e)
		{
			throw new ScriptExecutionException("Error while reading process output", e)
					.setDatabase(database)
					.setExecutor(this);
		}

		return tail;
	}
}
