//Include button push handler
var pre,tmp,count=0;

function show_confirm()
{
var r=confirm("保存？");
if (r==true)
  {
	getAreaContent();
	form1.SaveCheck.checked =true;
	alert("保存完了");
  }
else
  {
	form1.data.value=tmp;
  //form1.SaveCheck.checked =false;
  }
}

function turn_back()
{
  form1.data.value=form1.data.defaultValue;
}

function getAreaContent()
{
	if (count==0){ 
			pre=form1.data.defaultValue;
			tmp=form1.data.value;
			count++;
			}
	else{
			pre=tmp;
			tmp=form1.data.value;
			count++;
	}
	//alert(pre);
}

function getRightContent()
{
	form1.dataTrick.value=tmp;
}