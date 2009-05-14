/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.openjpa.persistence.criteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.QueryBuilder;
import javax.persistence.criteria.QueryBuilder.Trimspec;

import org.apache.openjpa.kernel.exps.ExpressionFactory;
import org.apache.openjpa.kernel.exps.Literal;
import org.apache.openjpa.kernel.exps.Value;
import org.apache.openjpa.kernel.jpql.JPQLExpressionBuilder;
import org.apache.openjpa.persistence.meta.MetamodelImpl;

public class Expressions {
	
	static Value toValue(ExpressionImpl<?> e, ExpressionFactory factory, 
		MetamodelImpl model) {
		Value v = e.toValue(factory, model);
		v.setImplicitType(e.getJavaType());
		v.setAlias(e.getAlias());
		return v;
	}
	
	/**
	 * Unary Functional Expression applies a unary function on a input
	 * Expression.
	 *
	 * @param <X> the type of the resultant expression
	 */
    public static class UnaryFunctionalExpression<X> 
        extends ExpressionImpl<X> {
        protected ExpressionImpl<?> e;
        
        public UnaryFunctionalExpression(Class<X> t, Expression<?> e) {
            super(t);
            this.e  = (ExpressionImpl<?>)e;
        }
        
        public UnaryFunctionalExpression(Expression<X> e) {
            this(e.getJavaType(), e);
        }
    }
    
    /**
     * Binary Functional Expression applies a binary function on a pair of
     * input Expression.
     * 
     *
	 * @param <X> the type of the resultant expression
     */
    public static class BinarayFunctionalExpression<X> 
        extends ExpressionImpl<X>{
        protected ExpressionImpl<?> e1;
        protected ExpressionImpl<?> e2;
        
        public BinarayFunctionalExpression(Class<X> t, Expression<?> x, 
        		Expression<?> y) {
            super(t);
            e1 = (ExpressionImpl<?>)x;
            e2 = (ExpressionImpl<?>)y;
        }
    }

    
    /**
     * Binary Logical Expression applies a binary function on a pair of
     * input Expression to generate a Predicate.
     *
     */
   public static class BinaryLogicalExpression extends PredicateImpl{
        protected ExpressionImpl<?> e1;
        protected ExpressionImpl<?> e2;
        
        public BinaryLogicalExpression(Expression<?> x, Expression<?> y) {
            super();
            e1 = (ExpressionImpl<?>)x;
            e2 = (ExpressionImpl<?>)y;
        }
    }
    
    
    public static class Abs<X> extends UnaryFunctionalExpression<X> {
    	public  Abs(Expression<X> x) {
    		super(x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.abs(Expressions.toValue(e, factory, model));
        }
    }
    
    public static class Count extends UnaryFunctionalExpression<Long> {
    	private boolean _distinct; 
    	public  Count(Expression<?> x) {
    		this(x, false);
    	}
    	
    	public  Count(Expression<?> x, boolean distinct) {
    		super(Long.class, x);
    		_distinct = distinct;
    		
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            Value v = factory.count(Expressions.toValue(e, factory, model));
            return _distinct ? factory.distinct(v) : v;
        }
    }

    public static class Avg extends UnaryFunctionalExpression<Double> {
    	public  Avg(Expression<?> x) {
    		super(Double.class, x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.avg(Expressions.toValue(e, factory, model));
        }
    }
    
    public static class Sqrt extends UnaryFunctionalExpression<Double> {
    	public  Sqrt(Expression<? extends Number> x) {
    		super(Double.class, x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.sqrt(Expressions.toValue(e, factory, model));
        }
    }
    
    public static class Max<X> extends UnaryFunctionalExpression<X> {
    	public  Max(Expression<X> x) {
    		super(x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.max(Expressions.toValue(e, factory, model));
        }
    }

    public static class Min<X> extends UnaryFunctionalExpression<X> {
    	public  Min(Expression<X> x) {
    		super(x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.min(Expressions.toValue(e, factory, model));
        }
    }
    
    public static class Size extends UnaryFunctionalExpression<Integer> {
    	public  Size(Expression<? extends Collection<?>> x) {
    		super(Integer.class, x);
    	}
    	
    	public  Size(Collection<?> x) {
    		this(new Constant<Collection<?>>(x));
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.size(Expressions.toValue(e, factory, model));
        }
    }

    public static class Cast<B> extends UnaryFunctionalExpression<B> {
    	Class<B> b;
    	public Cast(Expression<?> x, Class<B> b) {
    		super(b, x);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.cast(Expressions.toValue(e, factory, model), b);
        }
    }
    public static class Concat extends BinarayFunctionalExpression<String> {
    	public Concat(Expression<String> x, Expression<String> y) {
    		super(String.class, x, y);
    	}
    	
    	public Concat(Expression<String> x, String y) {
    		this(x, new Constant<String>(y));
    	}
    	
    	public Concat(String x, Expression<String> y) {
    		this(new Constant<String>(x), y);
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.concat(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class Substring extends UnaryFunctionalExpression<String> {
    	private ExpressionImpl<Integer> from;
    	private ExpressionImpl<Integer> len;
    	
    	public Substring(Expression<String> s, Expression<Integer> from, 
    			Expression<Integer> len) {
    		super(s);
    		this.from = (ExpressionImpl<Integer>)from;
    		this.len  = (ExpressionImpl<Integer>)len;
    	}
    	
    	public Substring(Expression<String> s) {
    		this(s, (Expression<Integer>)null, (Expression<Integer>)null);
    	}
    	
    	public Substring(Expression<String> s, Integer from) {
    		this(s, new Constant<Integer>(from), null);
    	}
    	
    	public Substring(Expression<String> s, Integer from, Integer len) {
            this(s, new Constant<Integer>(from), new Constant<Integer>(len));
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return JPQLExpressionBuilder.convertSubstringArguments(factory, 
            	Expressions.toValue(e, factory, model), 
            	from == null ? null : from.toValue(factory, model), 
            	len == null ? null : len.toValue(factory, model));
        }
    }
    
    public static class Trim extends BinarayFunctionalExpression<String> {
    	static Expression<Character> defaultTrim = new Constant<Character>
    	   (Character.class, new Character(' '));
    	static Trimspec defaultSpec = Trimspec.BOTH;
    	Trimspec ts;
    	public Trim(Expression<String> x, Expression<Character> y, 
    		Trimspec ts) {
    		super(String.class, x, y);
    		this.ts = ts;
    	}
    	
    	public Trim(Expression<String> x, Expression<Character> y) {
    		this(x, y, defaultSpec);
    	}
    	
    	public Trim(Expression<String> x) {
    		this(x, defaultTrim, defaultSpec);
    	}
    	
    	public Trim(Expression<String> x, Character t) {
            this(x, new Constant<Character>(Character.class, t), defaultSpec);
    	}
    	
    	public Trim(Expression<String> x, Character t, Trimspec ts) {
    		this(x, new Constant<Character>(Character.class, t), ts);
    	}
    	
    	public Trim(Expression<String> x, Trimspec ts) {
    		this(x, defaultTrim, ts);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
    		Boolean spec = null;
    		if (ts != null) {
    			switch (ts) {
    			case LEADING : spec = true; break;
    			case TRAILING : spec = false; break;
    			case BOTH : spec = null; break;
    			}
    		}
            return factory.trim(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model), spec);
        }
    }


    
    public static class Sum<N extends Number> 
        extends BinarayFunctionalExpression<N> {
    	public Sum(Expression<? extends Number> x, 
    		Expression<? extends Number> y) {
    		super((Class<N>)x.getJavaType(), x, y);
    	}
    	
    	public Sum(Expression<? extends Number> x) {
    		this(x, (Expression<? extends Number>)null);
    	}
    	

    	public Sum(Expression<? extends Number> x, Number y) {
    		this(x, new Constant<Number>(Number.class, y));
    	}

    	public Sum(Number x, Expression<? extends Number> y) {
    		this(new Constant<Number>(Number.class, x), y);
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
    		return (e2 == null) 
            ?   factory.sum(Expressions.toValue(e1, factory, model))
    		:   factory.add(
    			   Expressions.toValue(e1, factory, model), 
    			   Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class Product<N extends Number> 
        extends BinarayFunctionalExpression<N> {
    	public Product(Expression<? extends Number> x, 
    		Expression<? extends Number> y) {
    		super((Class<N>)x.getJavaType(), x, y);
    	}

    	public Product(Expression<? extends Number> x, Number y) {
    		this(x, new Constant<Number>(Number.class, y));
    	}

    	public Product(Number x, Expression<? extends Number> y) {
    		this(new Constant<Number>(Number.class, x), y);
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.multiply(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class Diff<N extends Number> 
        extends BinarayFunctionalExpression<N> {
    	public Diff(Expression<? extends Number> x, 
    		Expression<? extends Number> y) {
    		super((Class<N>)x.getJavaType(), x, y);
    	}

    	public Diff(Expression<? extends Number> x, Number y) {
    		this(x, new Constant<Number>(Number.class, y));
    	}

    	public Diff(Number x, Expression<? extends Number> y) {
    		this(new Constant<Number>(Number.class, x), y);
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.subtract(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model));
        }
    }

    
    public static class Quotient<N extends Number> 
        extends BinarayFunctionalExpression<N> {
    	public Quotient(Expression<? extends Number> x, 
    		Expression<? extends Number> y) {
    		super((Class<N>)x.getJavaType(), x, y);
    	}

    	public Quotient(Expression<? extends Number> x, Number y) {
    		this(x, new Constant<Number>(y));
    	}

    	public Quotient(Number x, Expression<? extends Number> y) {
    		this(new Constant<Number>(x), y);
    	}
    	
    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.divide(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model));
        }
    }



    public static class Mod extends BinarayFunctionalExpression<Integer> {
    	public  Mod(Expression<Integer> x, Expression<Integer> y) {
    		super(Integer.class, x,y);
    	}
    	public  Mod(Expression<Integer> x, Integer y) {
    		this(x,new Constant<Integer>(Integer.class, y));
    	}
    	public  Mod(Integer x, Expression<Integer> y) {
    		this(new Constant<Integer>(Integer.class, x),y);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.mod(
            	Expressions.toValue(e1, factory, model), 
            	Expressions.toValue(e2, factory, model));
        }
    }

    public static class CurrentDate extends ExpressionImpl<java.sql.Date> {
    	public  CurrentDate() {
    		super(java.sql.Date.class);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.getCurrentDate();
        }
    }
    
    public static class CurrentTime extends ExpressionImpl<java.sql.Time> {
    	public  CurrentTime() {
    		super(java.sql.Time.class);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.getCurrentTime();
        }
    }
    
    public static class CurrentTimestamp 
        extends ExpressionImpl<java.sql.Timestamp> {
    	public  CurrentTimestamp() {
    		super(java.sql.Timestamp.class);
    	}

    	@Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.getCurrentTimestamp();
        }
    }

    public static class Equal extends BinaryLogicalExpression {
        public <X,Y> Equal(Expression<X> x, Expression<Y> y) {
            super(x,y);
        }
        
        public <X> Equal(Expression<X> x, Object y) {
            this(x, new Constant<Object>(Object.class, y));
        }
        
        @Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
                return factory.equal(
                	Expressions.toValue(e1, factory, model), 
                	Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class GreaterThan extends BinaryLogicalExpression {
        public <X,Y> GreaterThan(Expression<X> x, Expression<Y> y) {
            super(x,y);
        }
        
        public <X> GreaterThan(Expression<X> x, Object y) {
            this(x, new Constant<Object>(Object.class, y));
        }
        
        @Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
                return factory.greaterThan(
                	Expressions.toValue(e1, factory, model), 
                	Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class GreaterThanEqual extends BinaryLogicalExpression {
        public <X,Y> GreaterThanEqual(Expression<X> x, Expression<Y> y) {
            super(x,y);
        }
        
        public <X> GreaterThanEqual(Expression<X> x, Object y) {
            this(x, new Constant<Object>(Object.class, y));
        }
        
        @Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
                return factory.greaterThanEqual(
                    Expressions.toValue(e1, factory, model), 
                    Expressions.toValue(e2, factory, model));
        }
    }

   
    public static class LessThan extends BinaryLogicalExpression {
        public <X,Y> LessThan(Expression<X> x, Expression<Y> y) {
            super(x,y);
        }
        
        public <X> LessThan(Expression<X> x, Object y) {
            this(x, new Constant<Object>(Object.class, y));
        }
        
        @Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
                return factory.lessThan(
                    Expressions.toValue(e1, factory, model), 
                    Expressions.toValue(e2, factory, model));
        }
    }
    
    public static class LessThanEqual extends BinaryLogicalExpression {
        public <X,Y> LessThanEqual(Expression<X> x, Expression<Y> y) {
            super(x,y);
        }
        
        public <X> LessThanEqual(Expression<X> x, Object y) {
            this(x, new Constant<Object>(Object.class, y));
        }
        
        @Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
                return factory.lessThanEqual(
                	Expressions.toValue(e1, factory, model), 
                    Expressions.toValue(e2, factory, model));

        }
    }

    public static class Between<Y extends Comparable<Y>> 
        extends PredicateImpl.And {
        public Between(Expression<? extends Y> v, Expression<? extends Y> x,
                Expression<? extends Y> y) {
            super(new GreaterThanEqual(v,x), new LessThanEqual(v,y));
        }
        
        public Between(Expression<? extends Y> v, Y x,
                Y y) {
            this(v, new Constant<Y>(x), new Constant<Y>(y));
        }
    }
    
    public static class Constant<X> extends ExpressionImpl<X> {
        public final Object arg;
        public Constant(Class<X> t, X x) {
            super(t);
            this.arg = x;
        }
        
        public Constant(X x) {
        	this((Class<X>)x.getClass(),x);
        }
        
        @Override
        public Value toValue(ExpressionFactory factory, MetamodelImpl model) {
            return factory.newLiteral(arg, 1);
        }
        
    }
    
    public static class IsEmpty extends PredicateImpl {
    	ExpressionImpl<?> collection;
    	public IsEmpty(Expression<?> collection) {
    		super();
    		this.collection = (ExpressionImpl<?>)collection;
    	}
    	
        @Override
        public org.apache.openjpa.kernel.exps.Expression toKernelExpression(
        	ExpressionFactory factory, MetamodelImpl model) {
            return factory.isEmpty(
            	Expressions.toValue(collection, factory, model));
        }
    }
    
    public static class IsMember<E> extends PredicateImpl {
    	ExpressionImpl<E> element;
    	ExpressionImpl<?> collection;
    	
    	public IsMember(Class<E> t, Expression<E> element, 
    		Expression<?> collection) {
    		this.element = (ExpressionImpl<E>)element;
    		this.collection = (ExpressionImpl<?>)collection;
    	}
    	
    	public IsMember(Class<E> t, E element, Expression<?> collection) {
    		this(t, new Constant<E>(element), collection);
    	}
    	
    	public IsMember(E element, Expression<?> collection) {
    		this((Class<E>)element.getClass(), element, collection);
    	}
    	
        @Override
        public org.apache.openjpa.kernel.exps.Expression toKernelExpression(
        	ExpressionFactory factory, MetamodelImpl model) {
            return factory.contains(factory.getThis(),
            	Expressions.toValue(collection, factory, model));
        }
    }
    
    public static class Like extends PredicateImpl {
    	ExpressionImpl<String> str;
    	ExpressionImpl<String> pattern;
    	ExpressionImpl<Character> escapeChar;
    	static ExpressionImpl<Character> defaultEscape = 
    		new Constant<Character>(new Character(' '));
    	
    	public Like(Expression<String> x, Expression<String> pattern,
    	        Expression<Character> escapeChar) {
    		super();
    		this.str = (ExpressionImpl<String>)x;
    		this.pattern = (ExpressionImpl<String>)pattern;
    		this.escapeChar = (ExpressionImpl<Character>)escapeChar;
    	}
    	
    	public Like(Expression<String> x, Expression<String> pat, char esc) {
    		this(x, pat, new Constant<Character>(Character.class, esc));
    	}
    	
    	public Like(Expression<String> x, Expression<String> pattern) {
    		this(x, pattern, defaultEscape);
    	}
    	
    	public Like(Expression<String> x, String pattern) {
    		this(x, new Constant<String>(pattern), defaultEscape);
    	}
    	public Like(Expression<String> x, String pat,  
    		Expression<Character> esc) {
    		this(x, new Constant<String>(pat), esc);
    	}
    	public Like(Expression<String> x, String pat,  Character esc) {
            this(x, new Constant<String>(pat), new Constant<Character>(esc));
    	}

    	@Override
        public org.apache.openjpa.kernel.exps.Expression toKernelExpression(
        	ExpressionFactory factory, MetamodelImpl model) {
            return factory.matches(
            	Expressions.toValue(str, factory, model), 
            	Expressions.toValue(pattern, factory, model), "_", "%", 
            	Expressions.toValue(escapeChar, factory, model).toString());
        }
    }
    
    public static class Coalesce<T> extends ExpressionImpl<T> 
    	implements QueryBuilder.Coalesce<T> {
    	private List<Expression<? extends T>> values = 
    		new ArrayList<Expression<? extends T>>();
    	
    	public Coalesce() {
    		super(null);
    	}
    	
    	public Coalesce(Class<T> cls) {
    		super(cls);
    	}
    	
        public Coalesce<T> value(T value) {
        	return value(new Constant<T>(value));
        }
        
        public Coalesce<T> value(Expression<? extends T> value) {
        	values.add(value); 
        	return this;
        }

    	
    	@Override
        public org.apache.openjpa.kernel.exps.Value toValue(
        	ExpressionFactory factory, MetamodelImpl model) {
    		Value[] vs = new Value[values.size()];
    		int i = 0;
    		for (Expression<?> e : values)
    	        vs[i++] = Expressions.toValue((ExpressionImpl<?>)e, 
    	           factory, model);
            return factory.coalesceExpression(vs);
        }
    }
    
    public static class IsNull extends PredicateImpl {
    	ExpressionImpl<?> e;
    	public IsNull(ExpressionImpl<?> e) {
    		super();
    		this.e = e;
    	}
    	
    	@Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
    		return factory.equal(
    			Expressions.toValue(e, factory, model), 
    			factory.getNull());
    	}
    }
    
    public static class IsNotNull extends PredicateImpl {
    	ExpressionImpl<?> e;
    	public IsNotNull(ExpressionImpl<?> e) {
    		super();
    		this.e = e;
    	}
    	
    	@Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
    		return factory.notEqual(
    			Expressions.toValue(e, factory, model), 
    			factory.getNull());
    	}
    }
    
    
    public static class In<T> extends PredicateImpl.Or 
    	implements QueryBuilder.In<T> {
    	ExpressionImpl<?> e;
    	public In(Expression<?> e) {
    		super((Predicate[])null);
    		this.e = (ExpressionImpl<?>)e;
    	}
    	
    	public Expression<T> getExpression() {
    		return null;
    	}

    	public In<T> value(T value) {
    		add(new Expressions.Equal(e,value));
        	return this;
    	}

    	public In<T> value(Expression<? extends T> value) {
    		add(new Expressions.Equal(e,value));
        	return this;
    	}
    
    	@Override
        org.apache.openjpa.kernel.exps.Expression toKernelExpression(
            ExpressionFactory factory, MetamodelImpl model) {
    		IsNotNull notNull = new Expressions.IsNotNull(e);
    		return factory.and(
     		    super.toKernelExpression(factory, model),
    		    notNull.toKernelExpression(factory, model));
    	}
    }
    
    public static class Case<T> extends ExpressionImpl<T> 
    implements QueryBuilder.Case<T> {
        private List<Expression<? extends T>> thens = 
            new ArrayList<Expression<? extends T>>();

        private List<Expression<Boolean>> whens = 
            new ArrayList<Expression<Boolean>>();

        private Expression<? extends T> otherwise;

        public Case() {
            super(null);
        }

        public Case(Class<T> cls) {
            super(cls);
        }

        public Case<T> when(Expression<Boolean> when, 
            Expression<? extends T> then) {
            whens.add(when);
            thens.add(then);
            return this;
        }

        public Case<T> when(Expression<Boolean> when, T then) {
            whens.add(when);
            Expression<? extends T> thenExpr = 
                new Expressions.Constant<T>(then);
            thens.add(thenExpr);
            return this;
        }

        public Case<T> otherwise(Expression<? extends T> otherwise) {
            this.otherwise = otherwise;
            return this;
        }

        public Case<T> otherwise(T otherwise) {
            this.otherwise = new Expressions.Constant<T>(otherwise);
            return this;
        }

        @Override
        public org.apache.openjpa.kernel.exps.Value toValue(
                ExpressionFactory factory, MetamodelImpl model) {
            int size = whens.size();
            org.apache.openjpa.kernel.exps.Expression[] exps = 
                new org.apache.openjpa.kernel.exps.Expression[size];
            for (int i = 0; i < size; i++) {
                org.apache.openjpa.kernel.exps.Expression expr = 
                    ((Expressions.BinaryLogicalExpression)whens.get(i)).
                    toKernelExpression(factory, model);
                Value action = ((ExpressionImpl<?>)thens.get(i)).
                    toValue(factory, model);
                exps[i] = factory.whenCondition(expr, action);
            }

            Value other = ((ExpressionImpl<?>)otherwise).
                toValue(factory, model);

            return factory.generalCaseExpression(exps, other);
        }
    }

    public static class SimpleCase<C,R> extends ExpressionImpl<R> 
    implements QueryBuilder.SimpleCase<C,R> {
        private List<Expression<? extends R>> thens = 
            new ArrayList<Expression<? extends R>>();

        private List<C> whens = new ArrayList<C>();

        private Expression<? extends R> otherwise;

        private Expression<? extends R> caseOperand;

        public SimpleCase() {
            super(null);
        }

        public SimpleCase(Class<R> cls) {
            super(cls);
        }
        
        public SimpleCase(Expression<? extends R> expr) {
            super(null);
            this.caseOperand = expr;
        }
        public Expression getExpression() {
            return caseOperand;
        }

        public SimpleCase<C,R> when(C when, Expression<? extends R> then) {
            whens.add(when);
            thens.add(then);
            return this;
        }

        public SimpleCase<C,R> when(C when, R then) {
            whens.add(when);
            Expression<? extends R> thenExpr = 
                new Expressions.Constant<R>(then);
            thens.add(thenExpr);
            return this;
        }

        public SimpleCase<C,R> otherwise(Expression<? extends R> otherwise) {
            this.otherwise = otherwise;
            return this;
        }

        public SimpleCase<C,R> otherwise(R otherwise) {
            this.otherwise = new Expressions.Constant<R>(otherwise);
            return this;
        }

        @Override
        public org.apache.openjpa.kernel.exps.Value toValue(
                ExpressionFactory factory, MetamodelImpl model) {
            Value caseOperandExpr = Expressions.toValue(
                (ExpressionImpl<?>)caseOperand, factory, model);
            int size = whens.size();
            org.apache.openjpa.kernel.exps.Expression[] exps = 
                new org.apache.openjpa.kernel.exps.Expression[size];
            for (int i = 0; i < size; i++) {
                org.apache.openjpa.kernel.exps.Literal val = null;
                //TODO: Boolean literal, String literal    
                val = factory.newLiteral(whens.get(i), Literal.TYPE_NUMBER);
                Value action = ((ExpressionImpl<?>)thens.get(i)).
                    toValue(factory, model);                
                exps[i] = factory.whenScalar(val, action);
            }

            Value other = ((ExpressionImpl<?>)otherwise).
                toValue(factory, model);
            return factory.simpleCaseExpression(caseOperandExpr, exps, other);
        }
    }
}
