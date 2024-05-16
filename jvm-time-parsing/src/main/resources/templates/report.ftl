<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>As time goes by - JVM date and time parsing</title>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
            crossorigin="anonymous"></script>

    <script src="https://code.jquery.com/jquery-3.7.1.min.js"
            integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo=" crossorigin="anonymous"></script>

    <link rel="stylesheet" href="https://cdn.jsdelivr.net/gh/iconoir-icons/iconoir@main/css/iconoir.css"/>

    <style>
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
    Overview of JVM (Java, Kotlin) date parsing function that are able to parse various datetime formats to the
    expected epoch seconds ${expectedTimestamp}

    <ul>
        <li>
            <i class="text-success iconoir-check-square-solid"></i>
            Datetime was successfully parsed to the expected timestamp ${expectedTimestamp}
        </li>
        <li>
            <i class="text-warning iconoir-check-square-solid"></i>
            Parsing did not fail, but the result differed from the expected result of ${expectedTimestamp}
        </li>
        <li>
            <i class="text-danger iconoir-xmark-circle-solid"></i>
            Parsing failed, see tooltip for error message
        </li>
    </ul>

</p>

<table class="table table-hover">
    <thead>
    <tr>
        <th scope="col">Timestamp</th>
        <#list headers as header>
            <th scope="col"><span class="vertical">${header}</span></th>
        </#list>
    </tr>
    </thead>

    <tbody>
    <#list results as result>
        <tr>
            <th scope="row">${result.timeFormat.time}</th>
            <#list result.parseResults as parseResult>
                <td class="<#if parseResult.status == "ok">text-success<#elseif parseResult.status == "diff">text-warning<#elseif parseResult.status == "failed">text-danger</#if>">
                    <#if parseResult.status == "ok">
                        <i class="iconoir-check-square-solid"></i>
                    <#elseif parseResult.status == "diff">
                        <i data-bs-toggle="tooltip" data-bs-title="diff ${(parseResult.diff)!"<unknown>"}"
                           class="iconoir-check-circle-solid"></i>
                    <#elseif parseResult.status == "failed">
                        <i data-bs-toggle="tooltip" data-bs-title="${(parseResult.error)!"<unknown>"}"
                           class="iconoir-xmark-circle-solid"></i>
                    </#if>
                </td>
            </#list>
        </tr>
    </#list>
    </tbody>
</table>

<script type="text/javascript">
    $(document).ready(function () {
        $("body").tooltip({selector: '[data-bs-toggle=tooltip]'});
    });
</script>

</body>
</html>