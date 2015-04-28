function assign(m)
{
  for(var k=0;k<m.length;k++)
  {
    if(m.length>40)
    {
      l1=[m[k].title,m[k].position.lat(),m[k].position.lng(),'pes_b1'];
      l.push(l1);
    }
    else if(m.length>30)
    {
      l2=[m[k].title,m[k].position.lat(),m[k].position.lng(),'pes_b2'];
      l.push(l1);
    }
    else if(m.length>10)
    {
      l3=[m[k].title,m[k].position.lat(),m[k].position.lng(),'pes_b3'];
      l.push(l3);
    }
    else if(m.length>5)
    {
      l4=[m[k].title,m[k].position.lat(),m[k].position.lng(),'pes_b4'];
      l.push(l4)
    }
    else if(m.length>2)
    {
      l5=[m[k].title,m[k].position.lat(),m[k].position.lng(),'pes_b4'];
      l.push(l5)
    }
    else
    {
      l6=[m[k].title,m[k].position.lat(),m[k].position.lng(),'9999'];
      l.push(l6)
    }
  }
  return l   
} 

