<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>As time goes by - JVM date and time parsing</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/iconoir-icons/iconoir@main/css/iconoir.css"/>

    <style>
        .css-tooltip {
            position: relative;
            display: inline-block;
        }

        .css-tooltip .css-tooltiptext {
            visibility: hidden;
            width: 120px;
            background-color: black;
            color: #fff;
            text-align: center;
            border-radius: 6px;
            padding: 5px 0;
            position: absolute;
            z-index: 1;
        }

        .css-tooltip:hover .css-tooltiptext {
            visibility: visible;
            width: 20rem;
        }

        .vertical {
            writing-mode: vertical-lr;
            display: inline-block;
            white-space: nowrap;
        }
    </style>
</head>
<body class="p-2">

<h1>As time goes by</h1>

<p class="lead">
    Overview of JVM (Java, Kotlin) date parsing functions that are able to parse various datetime formats to the
    expected epoch seconds ${expectedTimestamp}
    <mark>without providing the expected format</mark>
    .


</p>
<p class="lead">Benchmark for parse times
    The background color show the distribution of parsing times, with the average parsing as number of nanoseconds. The
    measurement is an average over ${benchmarkIterations} iterations per parse method.
</p>

<ul>
    <li>
        <i class="iconoir-check-circle-solid"></i>
        Datetime was successfully parsed to the expected timestamp ${expectedTimestamp}
    </li>
    <li>
        <i class="text-primary iconoir-warning-triangle-solid"></i>
        Parsing did not fail, but the result differed from the expected result of ${expectedTimestamp}, this can be a
        parsing error, or caused by missing precision/timezone in the input timestamp
    </li>
    <li>
        <i class="text-danger iconoir-xmark-circle-solid"></i>
        Parsing failed, see tooltip for error message
    </li>
</ul>

<h2>Datetime parse results</h2>

<table class="table table-hover">
    <thead>
    <tr>
        <th scope="col">Datetime</th>
        <#list columns as column>
            <th scope="col"><span class="vertical"><code>${column.name}</code></span></th>
        </#list>
    </tr>
    </thead>

    <tbody>
    <#list results as result>
        <tr>
            <th scope="row">${result.timeFormat.now}</th>
            <#list result.parseResults as parseResult>
                <td style="white-space: nowrap; background-color: ${parseResult.benchmarkHeatMapColor};"
                    class="<#if parseResult.status == "failed">text-danger<#elseif parseResult.status == "diff">text-primary</#if>">
                    <#if parseResult.status == "ok">
                        <i class="iconoir-check-circle-solid"></i>
                    <#elseif parseResult.status == "diff">
                        <div class="css-tooltip">
                            <i class="iconoir-warning-triangle-solid"></i>
                            <span class="css-tooltiptext">diff ${(parseResult.diff)!"<unknown>"}</span>
                        </div>
                    <#elseif parseResult.status == "failed">
                        <div class="css-tooltip">
                            <i class="iconoir-xmark-circle-solid"></i>
                            <span class="css-tooltiptext">${(parseResult.error)!"<unknown>"}</span>
                        </div>
                    </#if>
                    ${parseResult.benchmark?c}ns
                </td>
            </#list>
        </tr>
    </#list>
    </tbody>

    <tfoot>
    <tr>
        <th scope="col" class="text-end">Total <i class="text-success iconoir-check-square-solid"></i></th>
        <#list columns as column>
            <th scope="col">
                <div class="css-tooltip" style="text-decoration: underline dotted;">
                    ${column.successTotal}
                    <span class="css-tooltiptext">${column.name}</span>
                </div>
            </th>
        </#list>
    </tr>
    <tr>
        <th scope="col" class="text-end">Total <i class="text-primary iconoir-check-circle-solid"></i></th>
        <#list columns as column>
            <th scope="col">
                <div class="css-tooltip" style="text-decoration: underline dotted;">
                    ${column.diffTotal}
                    <span class="css-tooltiptext">${column.name}</span>
                </div>
            </th>
        </#list>
    </tr>
    </tfoot>
</table>


</body>
</html>