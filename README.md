# Recent Releases

Reports the recent releases of well known projects on GitHub.

## Command Line Flags

Command line flags are used as `-f value` or `--flag value`.

* `-t`, `--token` (required): [API token](https://github.com/settings/tokens/new) to increase rate limit
* `-s`, `--since` (optional): [ISO date](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_DATE) since when releases are interesting
* `-d`, `--duration` (optional, defaults to `14d`): `<integer>{h|d|w|m}` (hours, days, weeks, months) defines since when releases are interesting
