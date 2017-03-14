
var fs = require('fs');
const c = require('./callJavaMethod').calljavamethod;
var selectsFile = fs.readFileSync('./SearchConfig/ACMselects.json', 'utf-8');
var selectsData = JSON.parse(selectsFile);

var pre_selects = $("#pre_selects");

var selects_str = initSelectsStr();

//标识是否存在日期选择
var hasDate = false;

init();

/*----------Methods-------- */
function init() {
    //添加监听器
    $("ul.bodylist li").on('click', (e) => {
        $("ul.bodylist li").removeClass('active');
        $(".mainview").hide();
        e.currentTarget.setAttribute('class', 'active');
        var id = e.currentTarget.getAttribute('id');
        $("#" + id + "page").show();
    });

    $('label.radio').on('click', (e) => {
        var type = e.currentTarget.getAttribute('data-schedu');
        $('.schedu-check-group').hide();
        $('#' + type + '_check').show();
    });

    var index = pre_selects.attr("data-next-index");
    var origin = createFilter(index);
    pre_selects.attr("data-next-index", ++index);
    $("#selects").append(origin);

    initSearchGroup(index - 1);

    refreshConfigTable();

    initAvaliableDatabaseList();
}
//所有select的内容生成为html代码
function initSelectsStr() {
    var selects_str = {};
    for (var i in selectsData) {
        var _select = selectsData[i];
        var _str = '';
        for (var j in _select) {
            _str += '<option value=\"' + _select[j].value + '\">' + _select[j].name + '</option>';
        }
        selects_str[i] = _str;
    }
    return selects_str;
}
function initSearchGroup(index) {
    //select1 特殊处理
    var select1 = document.getElementById('select1_' + index);
    if (hasDate) {
        select1.innerHTML = selects_str.select1_without_date;
    } else {
        select1.innerHTML = selects_str.select1_normal;
    }
    for (var i in selects_str) {
        var select = document.getElementById(i + '_' + index);
        if (select !== null) {
            select.innerHTML = selects_str[i];
        }
    }
}

function createFilter(index) {
    return '<div id=\"wrp_' + index + '\" > ' +
        '<div id=\"select1_left_' + index + '\" class="wrp"> ' +
        '<label>Where</label> ' +
        '<select id=\"select1_' + index + '\" onchange=\"select1Change(this.selectedIndex, ' + index + ')\"></select> ' +
        '</div>' +
        '<div id=\"select1_right1_' + index + '\" class="wrp"> ' +
        '<select id=\"select2_' + index + '\"></select> ' +
        ' of the following words or phrases: ' +
        '<input id=\"wrp_input_' + index + '\" type=\"text\"/> ' +
        '</div>' +
        '<div id="buttons" class="wrp">' +
        (index == 0 ? '' : '<button id=\"delete_wrp_' + index + '\" onclick=\"deleteFilter(' + index + ');\" class=\"btn btn-sm btn-default\"><span class=\"glyphicon glyphicon-minus\"></span></button>') +
        '<button id=\"add_wrp_' + index + '\" onclick=\"addFilter(' + index + ');\" class=\"btn btn-sm btn-default\"><span class=\"glyphicon glyphicon-plus\"></span></button>' +
        '</div>' +
        '<div style="clear: both; margin: 0px"/>' +
        '</div>';
}

function createYearSelect(index) {
    return '<div id=\"select1_right2_' + index + '\">' +
        '<div class="wrp">' +
        '<select id=\"select3_' + index + '\" onchange=\"select3Change(this.selectedIndex, ' + index + ')\"></select>' +
        '<select id=\"years1_' + index + '\" onchange=\"years1Change(this.options[this.selectedIndex].text, ' + index + ')\" style="margin-left: 4px; margin-right: 4px"></select>' +
        '</div>' +
        '<div id=\"years_right_' + index + '\" class="wrp">' +
        ' to ' +
        '<select id=\"years2_' + index + '\" onchange=\"years2Change(this.options[this.selectedIndex].text, ' + index + ')\"></select>' +
        '</div>' +
        '<input type=\"text\" id=\"dte_' + index + '\" hidden=\"true\">' +
        '<input type=\"text\" id=\"bfr_' + index + '\" hidden=\"true\">' +
        '</div>';

}
function addFilter(index) {
    var _index = $("#pre_selects").attr("data-next-index");
    var divBlock = createFilter(_index);
    $("#wrp" + "_" + index).after(divBlock);
    initSearchGroup(_index);
    $("#pre_selects").attr("data-next-index", ++_index);
}

function deleteFilter(index) {
    $("#wrp" + "_" + index).remove();
}

function select1Change(selected, index) {
    var select1 = document.getElementById('select1_' + index);
    if (select1.options[selected].text == 'Publication year') {
        $("#select1_right1_" + index).hide();
        var right2 = createYearSelect(index);
        $("#select1_left_" + index).after(right2);
        initYears(index);
        hasDate = true;
    } else {
        $("#select1_right1_" + index).show();
        $("#select1_right2_" + index).remove();
        var length = $("#pre_selects").attr("data-next-index");
        hasDate = false;
        for (var i = 0; i < length; i++) {
            var _select1 = document.getElementById('select1_' + i);
            if (_select1 != null) {
                if (_select1.options[_select1.selectedIndex].text == 'Publication year') {
                    hasDate = true;
                    break;
                }
            }
        }
    }
    select1Switch(hasDate, index);
}
function select3Change(selected, index) {
    if (selectsData.select3[selected].name != 'is in the range') {
        $("#years_right_" + index).hide();
        if (selectsData.select3[selected].value == '=') {
            $("#dte").val($("#years1_" + index).val());
            $("#bfr").val($("#years1_" + index).val());
        } else if (selectsData.select3[selected].name == '<') {
            $("#dte").val('');
            $("#bfr").val($("#years1_" + index).val());
        } else if (selectsData.select3[selected].name == '>') {
            $("#dte").val($("#years1_" + index).val());
            $("#bfr").val('');
        }
    } else {
        $("#years_right_" + index).show();
        $("#dte").val($("#years1_" + index).val());
        $("#bfr").val($("#years2_" + index).val());
    }
}

function initYears(index) {
    var select3 = selectsData.select3;
    for (var i in select3) {
        $("#select3_" + index).append('<option value=\"' + select3[i].value + '\">' + select3[i].name + '</option>')
    }
    var date = new Date();
    var currentYear = date.getFullYear();
    $("#dte").val(1947);
    $("#bfr").val(currentYear);
    for (var i = 1947; i <= currentYear; i++) {
        $("#years1_" + index).append('<option value=\"' + i + '\">' + i + '</option>');
        var y = currentYear - i + 1947;
        $("#years2_" + index).append('<option value=\"' + y + '\">' + y + '</option>');
    }

}


function createSearchConfigJSON(schedu) {
    var length = $("#pre_selects").attr("data-next-index");
    var currentYear = new Date().getFullYear().valueOf();
    var data = {
        anyField: new Array(),
        title: new Array(),
        author: new Array(),
        digest: new Array(),
        dte: 1947,
        bfr: currentYear
    };
    var count = 0;
    for (var i = 0; i < length; i++) {
        var wrp = $("#wrp_" + i);
        if (wrp == null) {
            continue;
        } else {
            var select1Value = $("#select1_" + i).val();
            if (select1Value == 'Publication year') {
                //日期
                data.bfr = $("#brf").val();
                data.dte = $("#dte").val();
            } else {
                var select2Value = $("#select2_" + i).val();
                var inputValue = $("#wrp_input_" + i).val();
                data[select1Value][data[select1Value].length] =
                    { match: select2Value, value: inputValue };
                count += 1;
            }
        }
    }
    var jsonStr = JSON.stringify(data);
    var databaseListStr = $("#tagsinput").val();
    if (databaseListStr == "") {
        return alert('Must choose at least one database');
    }

    //if true, add to search config
    if (schedu) {
        var scheduPattern = getScheduPattern();
        if (scheduPattern == 'E') {
            return alert('Must create a schedu pattern');
        }
        c.call('method', {
            bean: 'searchConfigService',
            method: 'addNewSearchConfig',
            params: {
                jsonStr: jsonStr,
                schedu: scheduPattern,
                databases: databaseListStr
            }
        }, (data) => {
            $('#configModal').modal('hide');
            refreshConfigTable();
        })
    } else {
        //search right now
        c.call('method', {
            bean: 'searchService',
            method: 'search',
            params: {
                jsonStr: jsonStr,
                databases: databaseListStr
            }
        }, (data) => {
            var result = JSON.parse(data.toString());
            var dataList = new Array();
            $('#configModal').modal('hide');
            //结果列表更新
            var j = 0;
            for (var i in result) {
                dataList[j++] = {
                    doi: result[i].doi,
                    title: result[i].title,
                    authors: result[i].authors,
                    pdfurl: result[i].pdfURL,
                    source: result[i].source,
                    publicationDate: result[i].publicationDate
                }
            }
            setSearchResultTable();
            $('#resultTable').bootstrapTable('load', dataList);

        })

    }
}
function setSearchResultTable() {
    $('#resultTable').bootstrapTable({
        columns: [
            {
                field: 'doi',
                title: 'Doi',
                valign: 'middle',
                align: 'center'
            }, {
                field: 'title',
                title: 'Title',
                valign: 'middle',
                align: 'center'
            }, {
                field: 'authors',
                title: 'Authors',
                valign: 'middle',
                align: 'center'
            }, {
                field: 'pdfurl',
                title: 'PDF URL',
                valign: 'middle',
                align: 'center'
            }, {
                field: 'source',
                title: 'Source',
                valign: 'middle',
                align: 'center'
            }, {
                field: 'publicationDate',
                title: 'Publication Date',
                valign: 'middle',
                align: 'center'
            }
        ]
    });

}
function getScheduPattern() {
    var pattern = '';
    var weekChecked = document.getElementById('schedu_radio_week').checked;
    var checked = false;
    if (weekChecked) {
        pattern += 'W:';
        for (var i = 1; i <= 7; i++) {
            if (document.getElementById('week_' + i).checked) {
                checked = true;
                pattern += i + ',';
            }
        }
        if (pattern.endsWith(',')) {
            pattern = pattern.substring(0, pattern.length - 1);
        }
    } else {
        pattern += 'M:';
        for (var i = 1; i <= 3; i++) {
            if (document.getElementById('month_' + i).checked) {
                checked = true;
                pattern += (i == 1 ? '1' : (i == 2 ? '15' : (i == 3 ? 'L' : ''))) + ',';
            }
        }
        if (pattern.endsWith(',')) {
            pattern = pattern.substring(0, pattern.length - 1);
        }
    }
    if (!checked) {
        pattern = 'E';
    }
    return pattern;
}

function years1Change(year1Value, index) {
    var select3Value = $("#select3_" + index).val();
    var year2Value = $("#years2_" + index).val();
    if (select3Value == 'btw') {
        var year1 = $("#dte").val();
        if (year1 < year1Value) {
            $("#dte").val(year1);
        }
    } else if (select3Value == '=') {
        $("#dte").val(year1Value);
        $("#bfr").val(year1Value);
    } else if (select3Value == '<') {
        $("#dte").val('');
        $("#bfr").val(year1Value);
    } else if (select3Value == '>') {
        $("#dte").val(year1Value);
        $("#bfr").val('');
    }
}

function years2Change(year2Value, index) {
    $("#bfr").val(year2Value);
}

function refreshModal() {
    //selects置为初始
    $("#selects").empty();
    pre_selects.attr("data-next-index", "1");
    var origin = createFilter(0);
    $("#selects").append(origin);
    initSearchGroup(0);
    //schedu设置置为初始
    for (var i = 1; i <= 7; i++) {
        $('#week_' + i).attr('checked', false);
        if (i > 3) continue;
        $('#month_' + i).attr('checked', false);
    }
    document.getElementById('schedu_radio_week').click();
    // $('#schedu_radio_week').attr('checked', '');
    // $('#schedu_radio_month').removeAttr('checked');

    //database设置置为初始
    $('.bootstrap-tagsinput').children('input').attr('readonly', 'true');
    $('.bootstrap-tagsinput').children('input').val('');
    //设置按钮和输入框显示
    $('#scheduButton').show();
    $('#searchButton').hide();
    $('#modal_schedu').show();
}

function select1Switch(hasdate, index) {
    var length = $("#pre_selects").attr("data-next-index");
    var value = selects_str.select1_normal;
    if (hasdate) {
        value = selects_str.select1_without_date;
    }
    for (var i = 0; i < length; i++) {
        if (i != index) {
            var select1 = document.getElementById('select1_' + i);
            if (select1 != null) {
                var selected = select1.selectedIndex;
                if (select1 != null) {
                    if (select1.options[selected].text == 'Publication year') {
                        continue;
                    }
                    select1.innerHTML = value;
                    select1.selectedIndex = selected;
                }

            }
        }
    }
}


function refreshConfigTable() {
    var configList = new Array();
    c.call('method', {
        bean: 'searchConfigService',
        method: 'getConfigListString',

    }, (data) => {
        var dataList = JSON.parse(data.toString());
        for (var i = 0; i < dataList.length; i++) {
            configList[i] = {
                num: i + 1,
                time: dataList[i].createTime,
                schedu: dataList[i].schedulePattern,
                config: dataList[i].configJson,
                checkedDatabase: dataList[i].checkedDatabase,
                id: dataList[i].id
            }
        }

        $("#configTable").bootstrapTable({
            columns: [
                {
                    field: 'num',
                    title: 'Serial number',
                    valign: 'middle',
                    align: 'center'
                },
                {
                    field: 'time',
                    title: 'Create Time',
                    align: 'left',
                    valign: 'middle'
                },
                {
                    field: 'schedu',
                    title: 'Schedu',
                    align: 'center',
                    valign: 'middle'
                },
                {
                    field: 'config',
                    title: 'Search config',
                    align: 'left',
                    valign: 'middle'
                },
                {
                    field: 'checkedDatabase',
                    title: 'Databases',
                    align: 'left',
                    valign: 'middle'
                }, {
                    title: 'Operations',
                    align: 'center',
                    valign: 'middle',
                    events: operateEvents,
                    formatter: operateFormatter
                }]
        });
        $("#configTable").bootstrapTable('load', configList);
    });

}

function initAvaliableDatabaseList() {
    var file = fs.readFileSync('./DBConfigs/DB.json');
    var data = JSON.parse(file);
    var list = '';
    for (var i in data) {
        var shortName = data[i].shortName;
        list = list + '<li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:void(0);" onclick="addItemToDatabaseList(\'' + shortName + '\')">' + shortName + '</a></li>';
    }
    document.getElementById('database-list').innerHTML = list;

}

function addItemToDatabaseList(value) {
    $('#tagsinput').tagsinput('add', value);
}

function operateFormatter(value, row, index) {
    return [
        '<a class="remove" href="javascript:void(0)" title="Remove">',
        '<i class="glyphicon glyphicon-remove"></i>',
        '</a>'
    ].join('');
}
window.operateEvents = {
    'click .like': function (e, value, row, index) {
        alert('You click like action, row: ' + JSON.stringify(row));
    },
    'click .remove': function (e, value, row, index) {
        c.call('method', {
            bean: 'searchConfigService',
            method: 'deleteConfigLogic',
            params: {
                id: row.id
            }
        }, (data) => {
            console.log(data.toString());
            // If success, delete this row.
            if (JSON.parse(data.toString()).result == "SUCCESS") {

                $('#configTable').bootstrapTable('remove', {
                    field: 'id',
                    values: [row.id]
                });
                //refreshConfigTable();

            } else {
                alert('Delete failed: ' + data);
            }
            // If fail, alert.
        })
    }
}
function startSearch() {
    refreshModal();
    $('#scheduButton').hide();
    $('#searchButton').show();
    $('#modal_schedu').hide();

}

